package service;

import dao.CouponDAO;
import model.Coupon;

import java.util.Date;

public class CouponService {
    private CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = new CouponDAO();
    }

    public Coupon applyCoupon(String code) {
        Coupon c = couponDAO.findByCode(code);
        if (c == null) {
            System.out.println(" Mã giảm giá không tồn tại!");
            return null;
        }
        
        if (c.getCurrentUsage() >= c.getMaxUsage()) {
            System.out.println(" Mã giảm giá đã hết lượt sử dụng!");
            return null;
        }

        if (c.getExpiryDate() != null && c.getExpiryDate().before(new Date())) {
            System.out.println(" Mã giảm giá đã hết hạn!");
            return null;
        }

        return c;
    }
}
