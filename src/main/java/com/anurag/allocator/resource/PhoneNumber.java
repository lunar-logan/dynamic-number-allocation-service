package com.anurag.allocator.resource;

import com.anurag.allocator.service.AllocationService;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/phone-number")
public class PhoneNumber {
    private final AllocationService allocationService;

    public PhoneNumber(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PUT
    @Path("allocate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Timed
    public String allocateNew(@QueryParam("preferred") String preferredNumber) {
        if (preferredNumber == null || preferredNumber.trim().isEmpty()) {
            return allocationService.allocate(null);
        }
        return allocationService.allocate(Long.valueOf(preferredNumber));
    }
}
