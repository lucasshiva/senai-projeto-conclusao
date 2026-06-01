package br.com.senai.projetointegrador.features.incidents;

import br.com.senai.projetointegrador.features.course.CourseMapper;
import br.com.senai.projetointegrador.features.students.StudentMapper;
import br.com.senai.projetointegrador.features.users.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncidentMapper {
    private final UserMapper userMapper;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    public IncidentDto toDto(Incident incident) {
        return new IncidentDto(
                incident.getId(),
                courseMapper.toDto(incident.getCourse()),
                studentMapper.toDto(incident.getStudent()),
                userMapper.toDto(incident.getUser()),
                incident.getType(),
                incident.getDescription(),
                incident.getCreatedAt()
        );
    }
}
