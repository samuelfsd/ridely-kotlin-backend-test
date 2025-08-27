package tech.jaya.ridely.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "passenger")
class Passenger(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String = "",

    @Column(name = "email", unique = true, nullable = false)
    var email: String = "",

    @Column(name = "in_traveling", nullable = false)
    var inTraveling: Boolean = false,

    @Column(name = "activation_date", nullable = false)
    var activationDate: LocalDateTime = LocalDateTime.now(),
) {


}