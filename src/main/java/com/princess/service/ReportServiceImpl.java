package com.princess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.princess.domain.Board;
import com.princess.domain.CheckCondition.Display;
import com.princess.domain.CheckCondition.Type;
import com.princess.domain.Member;
import com.princess.domain.Product;
import com.princess.domain.Report;
import com.princess.persistence.BoardRepository;
import com.princess.persistence.MemberRepository;
import com.princess.persistence.ProductRepository;
import com.princess.persistence.ReportRepository;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Override
	public void insertReport(Report report) {
		reportRepository.save(report);
	}

	@Override
	public void updateReport(Report report) {
		reportRepository.save(report);
	}

	@Override
	public void deleteReport(Report report) {
		reportRepository.deleteById(report.getRptNo());
	}

	@Override
	public Report getReport(Report report) {
		return reportRepository.findById(report.getRptNo()).orElse(null);
	}

	@Override
	public Page<Report> getReportList(Pageable pageable, Display submit) {

		return reportRepository.findBySubmit(pageable, submit);
	}

	@Override
	public void changeReportStatus(Long rptNo, String type) {
		Report report = new Report();
		report = reportRepository.findById(rptNo).get();
		Long postNum = report.getPostNo();
		Member member = new Member();
		if (type.equals("Y")) {
			report.setSubmit(Display.Y);
			if (report.getType().equals(Type.PRODUCT)) {
				Product product = new Product();
				product = productRepository.findById(postNum).get();
				member = memberRepository.findById(product.getSalesId().getId()).get();
				product.setDisplay(Display.H);
				member.setBattery(member.getBattery() - 5);
				if (member.getBattery() <= 0) {
					member.setEnabled(Display.N);
					memberRepository.save(member);
				} else {
					memberRepository.save(member);
				}
				productRepository.save(product);

			} else {
				Board board = new Board();
				board = boardRepository.findById(postNum).get();
				member = memberRepository.findById(board.getUserId().getId()).get();
				board.setDisplay(Display.H);
				member.setBattery(member.getBattery() - 5);
				if (member.getBattery() <= 0) {
					member.setEnabled(Display.N);
					memberRepository.save(member);
				} else {
					memberRepository.save(member);
				}
				boardRepository.save(board);
			}
		} else {
			report.setSubmit(Display.N);
		}
		reportRepository.save(report);
	}
}