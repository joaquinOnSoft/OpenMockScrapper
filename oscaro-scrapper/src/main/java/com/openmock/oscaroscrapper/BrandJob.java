package com.openmock.oscaroscrapper;


import com.openmock.oscaroscrapper.dto.Brand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class BrandJob {
    private JobType type;
    private Brand brand;

    BrandJob(JobType type) {
        this.type = type;
    }
}
