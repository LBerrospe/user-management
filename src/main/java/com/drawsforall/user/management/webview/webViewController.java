package com.drawsforall.user.management.webview;

import com.drawsforall.user.management.business.UserMapper;
import com.drawsforall.user.management.business.UserService;
import com.drawsforall.user.management.persistence.entity.User;

import com.drawsforall.user.management.web.rest.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.IOException;

@Controller

public class webViewController {

    @Autowired
    private UserService service;
    @Autowired
    private UserMapper mapper;



    @RequestMapping(value = {"/"},method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("users",service.getUsers());


        return "index";
    }


/*
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestParam Long id, UserDTO users) {
        service.update(id, users);
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(@RequestParam Long id) {
        service.delete(id);
        return "redirect:/";
    }
*/
@RequestMapping(value = {"/addUser"},method = RequestMethod.GET)
public String create(Model model) {

    return "addUser";
}


    @RequestMapping(value = {"/adduser"},method = RequestMethod.POST)
    public String create(@ModelAttribute("newUser") UserDTO users) {
        service.createUser(users);
        return "redirect:/";
    }





}
