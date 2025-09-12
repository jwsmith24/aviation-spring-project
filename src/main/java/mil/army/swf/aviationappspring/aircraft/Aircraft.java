package mil.army.swf.aviationappspring.aircraft;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.army.swf.aviationappspring.pilot.Pilot;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String airframe;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Pilot pilot;
}
