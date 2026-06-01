package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.AdoptionRequest;
import javax.servlet.http.HttpSession;

public interface AdoptionService {
    Result apply(AdoptionRequest request, HttpSession session);
    Result myApplications(HttpSession session);
}