package com.folcademy.exampleapi.Services;

import com.folcademy.exampleapi.Exceptions.ExceptionKinds.UserBadRequestException;
import com.folcademy.exampleapi.Exceptions.ExceptionKinds.UserNotFoundException;
import com.folcademy.exampleapi.Models.Dtos.UserAddDTO;
import com.folcademy.exampleapi.Models.Dtos.UserEditDTO;
import com.folcademy.exampleapi.Models.Dtos.UserReadDTO;
import com.folcademy.exampleapi.Models.Entities.UserEntity;
import com.folcademy.exampleapi.Models.Mappers.UserMapper;
import com.folcademy.exampleapi.Models.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserReadDTO> findAll() {
        return userRepository
                .findAllByNameAndSurname("Nahuel", "Zalazar")
                .stream()
                .map(userMapper::userEntityToUserReadDTO)
                .collect(Collectors.toList());
    }

    public UserReadDTO add(UserAddDTO userAddDTO) {
        Boolean emailExist = userRepository.existsByEmail(userAddDTO.getEmail());
        if(emailExist) throw new UserBadRequestException("Ya existe un usuario con ese email.");

        return userMapper.userEntityToUserReadDTO(
                userRepository.save(
                        userMapper.userAddDTOToUserEntity(userAddDTO))
        );
    }

    public UserReadDTO findById(Integer userId) {
        try {
            return userRepository
                    .findById(userId)
                    .map(userEntity -> userMapper.userEntityToUserReadDTO(userEntity))
                    .orElseThrow(() -> new UserNotFoundException("No se encontro un usuario con ese identificador"));
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public UserReadDTO findUserById(Integer userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);

        if (userEntityOptional.isEmpty()) {
            throw new UserNotFoundException("No se encontro un usuario con ese identificador");
        }

        return userMapper.userEntityToUserReadDTO(userEntityOptional.get());
    }

    public UserReadDTO deleteById(Integer userId) {
        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException("No se encontro un usuario con ese identificador"));

            userRepository.delete(user);

            return userMapper.userEntityToUserReadDTO(user);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserReadDTO edit(Integer userId, UserEditDTO user) {
        try {
            UserEntity oldUser = userRepository.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException("No se encontro un usuario con ese identificador"));

            if (!user.getName().isBlank()) oldUser.setName(user.getName());
            if (!user.getSurname().isBlank()) oldUser.setSurname(user.getSurname());

            UserEntity newUser = userRepository.save(oldUser);

            return userMapper.userEntityToUserReadDTO(newUser);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
