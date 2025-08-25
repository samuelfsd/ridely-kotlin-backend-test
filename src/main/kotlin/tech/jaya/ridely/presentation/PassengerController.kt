package tech.jaya.ridely.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import tech.jaya.ridely.application.useCases.passengers.createPassenger.CreatePassengerUseCase
import tech.jaya.ridely.application.useCases.passengers.deletePassenger.DeletePassengerUseCase
import tech.jaya.ridely.application.useCases.passengers.getPassenger.GetPassengerUseCase
import tech.jaya.ridely.domain.dtos.PassengerCreation
import tech.jaya.ridely.domain.dtos.PassengerResponse

@RestController
@RequestMapping("/passengers")
class PassengerController(
    private val createPassengerUseCase: CreatePassengerUseCase,
    private val getPassengerUseCase: GetPassengerUseCase,
    private val deletePassengerUseCase: DeletePassengerUseCase,
) {

    @PostMapping
    fun create(
        @RequestBody passengerCreation: PassengerCreation,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<PassengerResponse> {
        val passengerResponse = createPassengerUseCase.execute(passengerCreation)

        val uri = uriBuilder.path("/passengers/${passengerResponse.id}").build().toUri()
        return ResponseEntity.created(uri).body(passengerResponse)
    }

    @GetMapping("/{id}")
    fun getPassenger(@PathVariable id: Long): ResponseEntity<PassengerResponse> {
        val passengerResponse = getPassengerUseCase.execute(id)
        return ResponseEntity.ok(passengerResponse)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        deletePassengerUseCase.execute(id)
        return ResponseEntity.noContent().build()
    }
}