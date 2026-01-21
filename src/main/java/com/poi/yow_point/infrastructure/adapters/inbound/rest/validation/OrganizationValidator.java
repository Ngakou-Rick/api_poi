package com.poi.yow_point.infrastructure.adapters.inbound.rest.validation;

import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.OrganizationDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OrganizationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return OrganizationDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrganizationDTO dto = (OrganizationDTO) target;

        // On ne valide que si les champs sont présents (permet les updates partiels)
        // Mais pour une création, le service s'assurera que les données sont suffisantes.

        if (dto.getOrgName() != null && dto.getOrgName().trim().isEmpty()) {
            errors.rejectValue("orgName", "orgName.empty", "Organization name cannot be empty if provided");
        }

        if (dto.getOrgCode() != null && dto.getOrgCode().trim().isEmpty()) {
            errors.rejectValue("orgCode", "orgCode.empty", "Organization code cannot be empty if provided");
        }
    }
}