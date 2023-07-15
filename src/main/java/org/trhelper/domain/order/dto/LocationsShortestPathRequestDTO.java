package org.trhelper.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LocationsShortestPathRequestDTO {
    private String acceptedBy;
    private String source;
    private String destination;
    private List<String> intermediateNodes;

    public LocationsShortestPathRequestDTO(String acceptedBy, String source, String destination, List<String> intermediateNodes) {
        this.acceptedBy = acceptedBy;
        this.source = source;
        this.destination = destination;
        this.intermediateNodes = intermediateNodes;
    }
}
