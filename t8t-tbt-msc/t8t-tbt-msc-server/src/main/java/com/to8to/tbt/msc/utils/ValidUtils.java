package com.to8to.tbt.msc.utils;

import com.to8to.sc.compatible.RPCException;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * @author edmund.yu
 */
public class ValidUtils {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static void valid(Object o) {
        if (o == null){
            throw new RPCException(MyExceptionStatus.PARAMS_CONTAINS_NULL);
        }
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            sb.append(constraintViolation.getPropertyPath());
            sb.append(constraintViolation.getMessage());
            sb.append(";");
        }
        if (StringUtils.isNotEmpty(sb)){
            throw new RPCException(sb.toString());
        }
}}
