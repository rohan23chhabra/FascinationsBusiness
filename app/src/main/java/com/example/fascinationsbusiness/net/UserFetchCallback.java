package com.example.fascinationsbusiness.net;

import com.example.fascinationsbusiness.core.User;

import java.util.List;

public interface UserFetchCallback {
    void onUsersFetched(List<User> userList);
}
