package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.entity.AgencyProfile;
import com.traveley.traveley_Backend.entity.TourPackage;
import com.traveley.traveley_Backend.repository.AgencyProfileRepo;
import com.traveley.traveley_Backend.repository.TourPackageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatbotToolService {
    private final TourPackageRepo tourPackageRepo;
    private final AgencyProfileRepo agencyProfileRepo;

    @Tool(description = "Get all available tour packages, prices, and details for Travely customers")
    public String getAvailablePackages(){
        List<TourPackage> tourPackages = tourPackageRepo.findAll();

        if (tourPackages.isEmpty()) {
            return "No packages available at the moment.";
        }
        StringBuilder sb = new StringBuilder("Available Tour Packages:\n");
        for(TourPackage p : tourPackages) {
            sb.append("Package Name: ").append(p.getTitle()).append("\n")
                    .append("Description: ").append(p.getDescription()).append("\n")
                    .append("Destination: ").append(p.getDestination()).append("\n")
                    .append("Price: $").append(p.getPrice()).append("\n")
                    .append("Duration: ").append(p.getDuration()).append("\n")
                    .append(" -----------------------------\n");
        }
        return sb.toString();
    }

    @Tool(description = "Get tour packages offered by a specific travel agency. " +
            "Use this tool when the user mentions an agency name like 'Jetwing', 'Aitken Spence', or 'Ceylon Tours'. " +
            "The input should be the exact agency name.")
    public String getTourPackagesByAgency(String agencyName){

        List<TourPackage> tourPackages = tourPackageRepo.findByAgencyProfile_AgencyName(agencyName);

        if (tourPackages.isEmpty()) {
            return "Sorry, we couldn't find any packages from the agency: " + agencyName;
        }
        StringBuilder sb = new StringBuilder("Available Tour Packages from" + agencyName + "\n");
        for(TourPackage tp : tourPackages) {
            sb.append("Package Name: ").append(tp.getTitle()).append("\n")
                    .append("Description: ").append(tp.getDescription()).append("\n")
                    .append("Destination: ").append(tp.getDestination()).append("\n")
                    .append("Price: $").append(tp.getPrice()).append("\n")
                    .append("Duration: ").append(tp.getDuration()).append("\n")
                    .append(" -----------------------------\n");
        }
        return sb.toString();
    }
    @Tool(description = "Generate a booking checkout link. ONLY use this AFTER you have explicitly asked for and received the traveler's name and mobile number. Do not guess or make up these details.")    public String generateBookingLink(String packageName, String travelerName, String contactNumber){
        return "BOOKING_ACTION|" + packageName + "|" + travelerName + "|" + contactNumber;
    }
}
