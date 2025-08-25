package tech.jaya.ridely.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.jaya.ridely.application.useCases.rides.acceptRide.AcceptRideUseCase
import tech.jaya.ridely.application.useCases.rides.cancelRide.CancelRideUseCase
import tech.jaya.ridely.application.useCases.rides.deleteRide.DeleteRideUseCase
import tech.jaya.ridely.application.useCases.rides.estimateRide.EstimateRideUseCase
import tech.jaya.ridely.application.useCases.rides.finishRide.FinishRideUseCase
import tech.jaya.ridely.application.useCases.rides.queueRide.QueueRideRequestUseCase
import tech.jaya.ridely.application.useCases.rides.refuseRide.RefuseRideUseCase
import tech.jaya.ridely.domain.dtos.*

@RestController
@RequestMapping("/rides")
class RideController(
    private val queueRideRequestUseCase: QueueRideRequestUseCase,
    private val estimateRideUseCase: EstimateRideUseCase,
    private val refuseRide: RefuseRideUseCase,
    private val cancelRide: CancelRideUseCase,
    private val finishRide: FinishRideUseCase,
    private val acceptRide: AcceptRideUseCase,
    private val deleteRide: DeleteRideUseCase,
) {

    @PostMapping("/estimate")
    fun estimateRide(@RequestBody req: EstimateRideRequest): ResponseEntity<EstimateRideResponse> {
        val response = estimateRideUseCase.execute(req)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/request-driver")
    fun requestDriver(@RequestBody req: RequestDriver): ResponseEntity<RideRequestStatusResponse> {
        val response = queueRideRequestUseCase.execute(req)
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/refuse-ride")
    fun refuseRide(@RequestBody req: ActionRideRequest): ResponseEntity<RefuseResponse> {
        val response = refuseRide.execute(req);
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/cancel-ride")
    fun deleteRide(@RequestBody req: ActionRideRequest): ResponseEntity<CancelResponse> {
        val response = cancelRide.execute(req)
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/finish-ride")
    fun finishRide(@RequestBody req: FinishRideRequest): ResponseEntity<FinishResponse> {
        val response = finishRide.execute(req)
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/accept-ride")
    fun acceptRide(@RequestBody req: ActionRideRequest): ResponseEntity<AcceptResponse> {
        val response = acceptRide.execute(req);
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        deleteRide.execute(id)
        return ResponseEntity.noContent().build();
    }
}
