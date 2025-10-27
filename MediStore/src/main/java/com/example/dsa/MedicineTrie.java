package com.example.dsa;

import com.example.entity.Medicine;
import java.util.*;

public class MedicineTrie {

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
        Medicine medicine = null;
    }

    private final TrieNode root = new TrieNode();

    // Insert medicine into Trie
    public void insert(Medicine medicine) {
        String name = medicine.getName().toLowerCase();
        TrieNode node = root;
        for (char ch : name.toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.medicine = medicine;
    }

    // Search by exact name
    public Medicine searchExact(String name) {
        String lower = name.toLowerCase();
        TrieNode node = root;
        for (char ch : lower.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) return null;
        }
        return node.isEndOfWord ? node.medicine : null;
    }

    // Search by prefix (returns suggestions)
    public List<Medicine> searchByPrefix(String prefix) {
        String lower = prefix.toLowerCase();
        TrieNode node = root;
        for (char ch : lower.toCharArray()) {
            node = node.children.get(ch);
            if (node == null) return Collections.emptyList();
        }
        List<Medicine> results = new ArrayList<>();
        collect(node, results);
        return results;
    }

    private void collect(TrieNode node, List<Medicine> result) {
        if (node.isEndOfWord && node.medicine != null) {
            result.add(node.medicine);
        }
        for (TrieNode child : node.children.values()) {
            collect(child, result);
        }
    }
}
