package com.capitalone.dashboard.rest;

import com.capitalone.dashboard.editors.CaseInsensitiveCodeQualityTypeEditor;
import com.capitalone.dashboard.model.CodeQuality;
import com.capitalone.dashboard.model.CodeQualityType;
import com.capitalone.dashboard.model.DataResponse;
import com.capitalone.dashboard.request.CodeQualityRequest;
import com.capitalone.dashboard.service.CodeQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class CodeQualityController {

    private final CodeQualityService codeQualityService;

    @Autowired
    public CodeQualityController(CodeQualityService codeQualityService) {
        this.codeQualityService = codeQualityService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(CodeQualityType.class, new CaseInsensitiveCodeQualityTypeEditor());
    }

    @RequestMapping(value = "/quality", method = GET, produces = APPLICATION_JSON_VALUE)
    public DataResponse<Iterable<CodeQuality>> qualityData(@Valid CodeQualityRequest request) {
        return codeQualityService.search(request);
    }

    @RequestMapping(value = "/quality/static-analysis", method = GET, produces = APPLICATION_JSON_VALUE)
    public DataResponse<Iterable<CodeQuality>> qualityStaticAnalysis(@Valid CodeQualityRequest request) {
        request.setType(CodeQualityType.StaticAnalysis);
        return codeQualityService.search(request);
    }

    @RequestMapping(value = "/quality/security-analysis", method = GET, produces = APPLICATION_JSON_VALUE)
    public DataResponse<Iterable<CodeQuality>> qualitySecurityAnalysis(@Valid CodeQualityRequest request) {
        request.setType(CodeQualityType.SecurityAnalysis);
        return codeQualityService.search(request);
    }
}
