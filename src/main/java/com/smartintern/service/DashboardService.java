package com.smartintern.service;

import com.smartintern.dto.DashboardStatsDto;
import com.smartintern.model.ApplicationStatus;
import com.smartintern.model.Role;
import com.smartintern.repository.ApplicationRepository;
import com.smartintern.repository.InternshipRepository;
import com.smartintern.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ApplicationRepository applicationRepository;

    public DashboardService(UserRepository userRepository,
                            InternshipRepository internshipRepository,
                            ApplicationRepository applicationRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
    }

    public DashboardStatsDto getStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalStudents(userRepository.countByRole(Role.STUDENT));
        stats.setTotalInternships(internshipRepository.countByActiveTrue());
        stats.setTotalApplications(applicationRepository.count());
        stats.setTotalSelected(applicationRepository.countByStatus(ApplicationStatus.SELECTED));
        stats.setTotalShortlisted(applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED));
        stats.setTotalPending(applicationRepository.countByStatus(ApplicationStatus.PENDING));
        stats.setTotalRejected(applicationRepository.countByStatus(ApplicationStatus.REJECTED));

        Map<String, Long> domainCounts = new HashMap<>();
        internshipRepository.findByActiveTrue().forEach(i -> {
            String domain = i.getDomain();
            domainCounts.put(domain, domainCounts.getOrDefault(domain, 0L) + 1);
        });
        stats.setDomainCounts(domainCounts);

        return stats;
    }
}
