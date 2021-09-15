package com.artificialbyte.animaliano.interfaces;

import com.artificialbyte.animaliano.dto.user.User;

public interface CRUDUser {
    void isRegister(Boolean e);
    void showMessage(String message);
    void showUser(String message);
    void getUserByEmail(User user);
}
