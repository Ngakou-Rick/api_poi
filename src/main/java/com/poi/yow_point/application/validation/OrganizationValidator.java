package com.poi.yow_point.application.validation;

import com.poi.yow_point.presentation.dto.OrganizationDTO;
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

        if (dto.getOrgName() == null || dto.getOrgName().trim().isEmpty()) {
            errors.rejectValue("orgName", "orgName.empty", "Organization name cannot be empty");
        }

        if (dto.getOrgCode() == null || dto.getOrgCode().trim().isEmpty()) {
            errors.rejectValue("orgCode", "orgCode.empty", "Organization code cannot be empty");
        }

        if (dto.getOrgType() == null || dto.getOrgType().trim().isEmpty()) {
            errors.rejectValue("orgType", "orgType.empty", "Organization type cannot be empty");
        }
    }
}