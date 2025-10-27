package com.example.controller;

import com.example.entity.Medicine;
import com.example.service.MedicineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // 🔍 Search exact medicine
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String name) {
        Medicine medicine = medicineService.searchExact(name);
        if (medicine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medicine '" + name + "' not found");
        }
        return ResponseEntity.ok(medicine);
    }

    // 💡 Suggest medicines by prefix
    @GetMapping("/suggest")
    public ResponseEntity<?> suggest(@RequestParam String prefix) {
        List<Medicine> matches = medicineService.searchByPrefix(prefix);
        if (matches.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No medicines found starting with '" + prefix + "'");
        }
        return ResponseEntity.ok(matches);
    }

    // ➕ Add single medicine (UPSERT)
    @PostMapping("/add")
    public ResponseEntity<?> addMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineService.addMedicine(medicine);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ➕ Add bulk medicines (UPSERT)
    @PostMapping("/add-bulk")
    public ResponseEntity<?> addMedicines(@RequestBody List<Medicine> medicines) {
        List<Medicine> saved = medicineService.addMedicines(medicines);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 📊 Get count of out-of-stock medicines
    @GetMapping("/out-of-stock/count")
    public ResponseEntity<Long> getOutOfStockCount() {
        long count = medicineService.getOutOfStockCount();
        return ResponseEntity.ok(count);
    }

    // 📋 Get list of out-of-stock medicines
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Medicine>> getOutOfStockMedicines() {
        List<Medicine> medicines = medicineService.getOutOfStockMedicines();
        return ResponseEntity.ok(medicines);
    }
}
