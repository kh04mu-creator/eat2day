package com.study.springboot.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.study.springboot.dao.restaurantDAO;
import com.study.springboot.dto.restaurantDTO;
import com.study.springboot.service.restaurantService;

@Controller
public class restaurantListController {
	@Autowired
	restaurantService service;
	@Autowired
	restaurantDAO dao;
	
	@GetMapping("/searchKeyword")
	@ResponseBody
	public Map<String, Object> search(
			
	    @RequestParam("keyword") String keyword,
	    @RequestParam("lat") double lat,
	    @RequestParam("lon") double lon,
	    @RequestParam(value = "page", defaultValue = "0") int page,
	    @RequestParam(value = "size", defaultValue = "10") int size    
	    
	) throws Exception {
		
		
	    return service.search(keyword, lat, lon, page, size);
	}
	
	@GetMapping("/searchList")
	public String searchPage(
	    @RequestParam("keyword") String keyword,
	    @RequestParam(value="lat", required=false) Double lat,
	    @RequestParam(value="lon", required=false) Double lon,
	    Model model
	) {
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("lat", lat);
	    model.addAttribute("lon", lon);
	    return "restaurantList";
	}
	@RequestMapping("/admin/addRestaurantForm")
	public String addRestaurantForm() {
		return "admin/addRestaurantForm";
	}
	
	@RequestMapping("/admin/addRestaurant")
	public String addRestaurant(restaurantDTO dto) {
		dao.addRestaurant(dto);
		
		return "admin/dashboard";
	}
	
	@RequestMapping("/admin/listRestaurant")
	public String listRestaurant(Model model) {
		
		model.addAttribute("list", dao.listRestaurant());
		
		return "admin/listRestaurant";
	}
	
	@RequestMapping("/admin/detailRestaurant")
	public String viewRestaurant(@RequestParam("id") int id, Model model) {
		model.addAttribute("view", dao.viewRestaurant(id));
		
		return "admin/detailRestaurant";
	}
	
	@RequestMapping("/admin/updateRestaurantForm")
	public String updateRestaurantForm(@RequestParam("id") int id, Model model) {
		model.addAttribute("edit", dao.viewRestaurant(id));
		
		return "admin/updateRestaurantForm";
	}
	
	@RequestMapping("/admin/updateRestaurant")
	public String updateRestaurant(restaurantDTO dto) {
		dao.updateRestaurant(dto);
		
		return "redirect:/admin/detailRestaurant?id=" + dto.getId();
	}
	
	@RequestMapping("/admin/deleteRestaurant")
	public String deleteRestaurnat(@RequestParam("id") int id) {
		dao.deleteRestaurant(id);
		
		return "redirect:/admin/listRestaurant";
	}
	
	@ResponseBody
	@RequestMapping("/autocomplete")
	public List<Map<String, String>>
	autocomplete(@RequestParam("keyword") String keyword) throws Exception{
		return service.autocomplete(keyword);
	}


}
