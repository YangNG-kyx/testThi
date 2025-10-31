package service;

import entity.SanPham;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SanPhamService {

    private final Map<String, SanPham> byMa = new HashMap<>();


    public SanPham add(SanPham sp) {
        if (sp == null) throw new IllegalArgumentException("San pham null");
        if (sp.getMa() == null || sp.getMa().isBlank()) throw new IllegalArgumentException("Ma rong");
        if (sp.getTen() == null || sp.getTen().isBlank()) throw new IllegalArgumentException("Ten rong");
        if (sp.getSoLuong() < 1 || sp.getSoLuong() > 100) throw new IllegalArgumentException("So luong 1..100");
        if (byMa.containsKey(sp.getMa())) throw new IllegalArgumentException("Ma da ton tai");
        byMa.put(sp.getMa(), sp);
        return sp;
    }

    public boolean existsByMa(String ma) {
        return byMa.containsKey(ma);
    }

    public int size() {
        return byMa.size();
    }

    public Optional<SanPham> findByMa(String ma) {
        return Optional.ofNullable(byMa.get(ma));
    }
}