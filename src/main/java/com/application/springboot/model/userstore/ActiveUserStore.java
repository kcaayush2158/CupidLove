package com.application.springboot.model.userstore;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActiveUserStore {

  private List<String> users;

     public ActiveUserStore() {
        this.users = new ArrayList<>();
    }


}
