package com.openmock.oscaroscrapper.pojo;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "core-label",
        "complement-label",
        "full-label-fragment",
        "full-label",
        "label"
})
public class Labels {

    @JsonProperty("core-label")
    private CoreLabel coreLabel;
    @JsonProperty("complement-label")
    private ComplementLabel complementLabel;
    @JsonProperty("full-label-fragment")
    private FullLabelFragment fullLabelFragment;
    @JsonProperty("full-label")
    private FullLabel fullLabel;
    @JsonProperty("label")
    private Label label;
    @JsonIgnore
    private final Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("core-label")
    public CoreLabel getCoreLabel() {
        return coreLabel;
    }

    @JsonProperty("core-label")
    public void setCoreLabel(CoreLabel coreLabel) {
        this.coreLabel = coreLabel;
    }

    @JsonProperty("complement-label")
    public ComplementLabel getComplementLabel() {
        return complementLabel;
    }

    @JsonProperty("complement-label")
    public void setComplementLabel(ComplementLabel complementLabel) {
        this.complementLabel = complementLabel;
    }

    @JsonProperty("full-label-fragment")
    public FullLabelFragment getFullLabelFragment() {
        return fullLabelFragment;
    }

    @JsonProperty("full-label-fragment")
    public void setFullLabelFragment(FullLabelFragment fullLabelFragment) {
        this.fullLabelFragment = fullLabelFragment;
    }

    @JsonProperty("full-label")
    public FullLabel getFullLabel() {
        return fullLabel;
    }

    @JsonProperty("full-label")
    public void setFullLabel(FullLabel fullLabel) {
        this.fullLabel = fullLabel;
    }

    @JsonProperty("label")
    public Label getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(Label label) {
        this.label = label;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
