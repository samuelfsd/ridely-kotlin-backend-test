package tech.jaya.ridely.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import tech.jaya.ridely.application.useCases.drivers.createDriver.CreateDriverUseCase
import tech.jaya.ridely.application.useCases.drivers.deleteDriver.DeleteDriverUseCase
import tech.jaya.ridely.application.useCases.drivers.getDriver.GetDriverUseCase
import tech.jaya.ridely.application.useCases.drivers.getLastRideByDriver.GetLastRideByDriverUseCase
import tech.jaya.ridely.domain.dtos.AcceptResponse
import tech.jaya.ridely.domain.dtos.DriverCreation
import tech.jaya.ridely.domain.dtos.DriverResponse

@RestController
@RequestMapping("/drivers")
class DriverController(
    private val getDriverUseCase: GetDriverUseCase,
    private val createDriverUseCase: CreateDriverUseCase,
    private val deleteDriverUseCase: DeleteDriverUseCase,
    private val getLastRideByDriverUseCase: GetLastRideByDriverUseCase,
) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<DriverResponse> {
        return ResponseEntity.ok(getDriverUseCase.execute(id))
    }

    @GetMapping("/{id}/get-rides")
    fun getRide(@PathVariable id: Long): ResponseEntity<AcceptResponse> {
        return ResponseEntity.ok(getLastRideByDriverUseCase.execute(id));
    }

    @PostMapping
    fun save(
        @RequestBody driverRequest: DriverCreation,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<DriverResponse> {
        val createdDriverDto = createDriverUseCase.execute(driverRequest)
        val uri = uriBuilder.path("/drivers/${createdDriverDto.id}").build().toUri()

        return ResponseEntity.created(uri).body(createdDriverDto)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        deleteDriverUseCase.execute(id);
        return ResponseEntity.noContent().build()
    }
}