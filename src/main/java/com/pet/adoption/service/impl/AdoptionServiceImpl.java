package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.dto.AdoptionRequest;
import com.pet.adoption.entity.Adoption;
import com.pet.adoption.entity.Pet;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.AdoptionRepository;
import com.pet.adoption.repository.PetRepository;
import com.pet.adoption.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private PetRepository petRepository;

    @Override
    public Result apply(AdoptionRequest request, HttpSession session) {
        User user = SessionUtils.getUser(session);
        Pet pet = petRepository.findById(request.getPetId()).orElse(null);
        if (pet == null || !"待领养".equals(pet.getStatus())) {
            return Result.error("该宠物不可申请领养");
        }
        Adoption adoption = new Adoption();
        adoption.setPetId(request.getPetId());
        adoption.setUserId(user.getId());
        adoption.setReason(request.getReason());
        adoption.setAddress(request.getAddress());
        adoption.setStatus("待审核");
        adoption.setApplyTime(new Date());
        adoptionRepository.save(adoption);
        return Result.success("申请已提交");
    }

    @Override
    public Result myApplications(HttpSession session) {
        User user = SessionUtils.getUser(session);
        List<Adoption> list = adoptionRepository.findByUserId(user.getId());
        return Result.success(list);
    }
}