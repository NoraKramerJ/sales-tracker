package com.salestracker.controller;

import com.salestracker.model.Sale;
import com.salestracker.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Sales", description = "Create, read, update and delete sales records")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // --- Sale endpoints ---

    @GetMapping
    @Operation(summary = "List all sales", description = "Returns every sale record.")
    @ApiResponse(responseCode = "200", description = "List of sales returned")
    public List<Sale> getAll() {
        return saleService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sale by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale found"),
            @ApiResponse(responseCode = "404", description = "Sale not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Sale> getById(
            @Parameter(description = "Id of the sale to retrieve", example = "1")
            @PathVariable Long id) {
        return saleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new sale")
    @ApiResponse(responseCode = "200", description = "Sale created")
    public Sale create(@RequestBody Sale sale) {
        return saleService.create(sale);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing sale")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale updated"),
            @ApiResponse(responseCode = "404", description = "Sale not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Sale> update(
            @Parameter(description = "Id of the sale to update", example = "1")
            @PathVariable Long id,
            @RequestBody Sale sale) {
        try {
            return ResponseEntity.ok(saleService.update(id, sale));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sale")
    @ApiResponse(responseCode = "204", description = "Sale deleted")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id of the sale to delete", example = "1")
            @PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
