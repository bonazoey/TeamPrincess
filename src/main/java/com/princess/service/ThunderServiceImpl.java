package com.princess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.princess.domain.Member;
import com.princess.domain.Product;
import com.princess.domain.QProduct;
import com.princess.domain.QSales;
import com.princess.domain.Review;
import com.princess.domain.Sales;
import com.princess.domain.Search;
import com.princess.domain.CheckCondition.Display;
import com.princess.domain.CheckCondition.Role;
import com.princess.domain.CheckCondition.YorN;
import com.princess.persistence.MemberRepository;
import com.princess.persistence.ProductRepository;
import com.princess.persistence.ReviewRepository;
import com.princess.persistence.SalesRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

@Service
public class ThunderServiceImpl implements ThunderService {

	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private SalesRepository saleseRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private MemberRepository memberRepo;
	
	// THUNDER Controller
		public Page<Sales> myThunderList(Search search, Pageable pageable) {
			BooleanBuilder builder = new BooleanBuilder();
			
			QProduct qProduct = QProduct.product;
			QSales qsales = QSales.sales;
			
//			if (search.getSearchCondition().equals("thunderId")) {
//				builder.and(qsales.thunderId.like("%" + search.getSearchKeyword() +"%"));
//			} else if (search.getSearchCondition().equals("ID")) {
//				builder.and(qsales.buyer.id.like("%" + search.getSearchKeyword() +"%"));
//			}
			
			
			return saleseRepo.findAll(builder, pageable);
		}
		
		public void thunderDelivery(Long productPno, Member member) {
			Sales thunderSales = saleseRepo.findByProductPNo(productPno);
			thunderSales.setThunderId(member.getId());
			System.out.println("thunderSales :"+thunderSales.toString());
			saleseRepo.save(thunderSales);
		}
		
		public List<Review> getreviewList() {
			return (List<Review>) reviewRepo.findAll();
		}
		
		public void deleteThunder(Member member) {
			Member findmember = memberRepo.findById(member.getId()).get();
			System.out.println("findmember : "+findmember.toString());
			findmember.setAuth(Role.MEMBER);
			memberRepo.save(findmember);
		}
		
		public void updateThunder(Member member) {
			Member findmember = memberRepo.findById(member.getId()).get();
			System.out.println("findmember : "+findmember.toString());
			findmember.setAuth(Role.THUNDER);
			memberRepo.save(findmember);
		}
		
		

}
