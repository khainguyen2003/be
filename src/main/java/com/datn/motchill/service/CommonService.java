package com.datn.motchill.service;

import com.datn.motchill.dto.actor.ActorDto;
import com.datn.motchill.dto.country.CountryDto;
import com.datn.motchill.dto.director.DirectorDto;
import com.datn.motchill.entity.Actor;
import com.datn.motchill.repository.ActorRepository;
import com.datn.motchill.repository.CountryRepository;
import com.datn.motchill.repository.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {
    private final CountryRepository countryRepo;
    private final ActorRepository castRepo;
    private final DirectorRepository directorRepo;

    public CommonService(CountryRepository countryRepo, ActorRepository castRepo, DirectorRepository directorRepo) {
        this.countryRepo = countryRepo;
        this.castRepo = castRepo;
        this.directorRepo = directorRepo;
    }

    public List<CountryDto> getAllCountries() {
        return countryRepo.findAll().stream()
                .map(c -> new CountryDto(c.getId(), c.getName(), c.getSlug()))
                .collect(Collectors.toList());
    }

    public List<ActorDto> getAllCasts() {
        return castRepo.findAll().stream()
                .map(c -> new ActorDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    public List<DirectorDto> getAllDirectors() {
        return directorRepo.findAll().stream()
                .map(d -> new DirectorDto(d.getId(), d.getName()))
                .collect(Collectors.toList());
    }
}
