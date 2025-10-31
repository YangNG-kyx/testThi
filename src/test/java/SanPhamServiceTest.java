import entity.SanPham;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SanPhamService;

import static org.junit.jupiter.api.Assertions.*;

class SanPhamServiceTest {

    private SanPhamService service;

    @BeforeEach
    void khoiTao() {
        service = new SanPhamService();
    }

    @Test
    void them_HopLeCanDuoi_Pass() {
        SanPham sp = new SanPham("SP01", "Ao", 2025, 100_000, 1, "Thoi trang");
        service.add(sp);
        assertTrue(service.existsByMa("SP01"));
    }

    @Test
    void them_HopLeCanTren_Pass() {
        SanPham sp = new SanPham("SP02", "Ao2", 2025, 100_000, 100, "Thoi trang");
        service.add(sp);
        assertEquals(1, service.size());
    }

    @Test
    void them_SaiDuoiCan_ThrowLoi() {
        SanPham sp = new SanPham("SP03", "Ao3", 2025, 100_000, 0, "Thoi trang");
        assertThrows(IllegalArgumentException.class, () -> service.add(sp));
    }

    @Test
    void them_SaiTrenCan_ThrowLoi() {
        SanPham sp = new SanPham("SP04", "Ao4", 2025, 100_000, 101, "Thoi trang");
        assertThrows(IllegalArgumentException.class, () -> service.add(sp));
    }

    @Test
    void them_TrungMa_ThrowLoi() {
        SanPham a = new SanPham("SP05", "Ao5", 2025, 100_000, 10, "Thoi trang");
        SanPham b = new SanPham("SP05", "Ao5-b", 2025, 100_000, 10, "Thoi trang");
        service.add(a);
        assertThrows(IllegalArgumentException.class, () -> service.add(b));
    }

    @Test
    void them_TenRong_ThrowLoi() {
        SanPham sp = new SanPham("SP06", "  ", 2025, 100_000, 10, "Thoi trang");
        assertThrows(IllegalArgumentException.class, () -> service.add(sp));
    }
}
