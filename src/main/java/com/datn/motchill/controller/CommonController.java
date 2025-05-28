package com.datn.motchill.controller;

import com.datn.motchill.dto.ActorDto;
import com.datn.motchill.dto.CountryDto;
import com.datn.motchill.dto.DirectorDto;
import com.datn.motchill.service.CommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(commonService.getAllCountries());
    }

    @GetMapping("/casts")
    public ResponseEntity<List<ActorDto>> getAllCasts() {
        return ResponseEntity.ok(commonService.getAllCasts());
    }

    @GetMapping("/directors")
    public ResponseEntity<List<DirectorDto>> getAllDirectors() {
        return ResponseEntity.ok(commonService.getAllDirectors());
    }
}
