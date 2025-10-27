package com.example.service;

import com.example.dsa.MedicineTrie;
import com.example.entity.Medicine;
import com.example.repository.MedicineRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineTrie medicineTrie = new MedicineTrie();

    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    // 🚀 Load all medicines into Trie on startup + log out-of-stock
    @PostConstruct
    public void loadTrie() {
        List<Medicine> medicines = medicineRepository.findAll();
        for (Medicine med : medicines) {
            medicineTrie.insert(med);
        }
        System.out.println("Loaded " + medicines.size() + " medicines into Trie.");

        // Log out-of-stock / low-stock
        List<Medicine> outOfStock = getOutOfStockMedicines();
        System.out.println("⚠️ Out/Low Stock Medicines: " + outOfStock.size());
        outOfStock.forEach(m ->
                System.out.println("- " + m.getName() + " (Qty: " + m.getQuantity() + ")")
        );
    }

    // ➕ Add or update medicine (UPSERT with quantity increment)
    public Medicine addMedicine(Medicine medicine) {
        Optional<Medicine> existingOpt = medicineRepository.findByNameIgnoreCase(medicine.getName());
        Medicine toSave;
        if (existingOpt.isPresent()) {
            Medicine existing = existingOpt.get();
            existing.setPrice(medicine.getPrice());
            // ✅ Increment instead of overwrite
            existing.setQuantity(existing.getQuantity() + medicine.getQuantity());
            toSave = existing;
        } else {
            toSave = medicine;
        }

        Medicine saved = medicineRepository.save(toSave);
        medicineTrie.insert(saved);
        return saved;
    }

    // ➕ Add multiple medicines (each one increments)
    public List<Medicine> addMedicines(List<Medicine> medicines) {
        List<Medicine> result = new ArrayList<>();
        for (Medicine med : medicines) {
            result.add(addMedicine(med)); // ✅ reuses increment logic
        }
        return result;
    }

    // 🔍 Search exact medicine by name
    public Medicine searchExact(String name) {
        long startTrie = System.nanoTime();
        Medicine trieResult = medicineTrie.searchExact(name);
        long endTrie = System.nanoTime();

        long startDb = System.nanoTime();
        Medicine dbResult = medicineRepository.findByNameIgnoreCase(name).orElse(null);
        long endDb = System.nanoTime();

        System.out.println("Trie search time (ns): " + (endTrie - startTrie));
        System.out.println("Database search time (ns): " + (endDb - startDb));

        // Prefer Trie result
        return trieResult != null ? trieResult : dbResult;
    }

    // 💡 Suggest medicines by prefix
    public List<Medicine> searchByPrefix(String prefix) {
        return medicineTrie.searchByPrefix(prefix);
    }

    // 📊 Count out-of-stock medicines (quantity < 10)
    public long getOutOfStockCount() {
        return medicineRepository.findAll()
                .stream()
                .filter(m -> m.getQuantity() < 10)
                .count();
    }

    // 📋 Get list of out-of-stock medicines (quantity < 10)
    public List<Medicine> getOutOfStockMedicines() {
        return medicineRepository.findAll()
                .stream()
                .filter(m -> m.getQuantity() < 10)
                .toList();
    }
}
