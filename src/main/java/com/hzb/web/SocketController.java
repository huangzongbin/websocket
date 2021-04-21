package com.hzb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author :huangZB
 * @date 2021/4/16
 * @Description
 */
@Controller
public class SocketController {

	@ResponseBody
	@GetMapping("test")
	public String test(){
		return "test";
	}


	@GetMapping("index")
	public String index(){
		return "index";
	}

}
