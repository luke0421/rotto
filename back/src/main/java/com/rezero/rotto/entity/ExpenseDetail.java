package com.rezero.rotto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expense_detail_tb")
public class ExpenseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_detail_code")
    private int expenseDetailCode;

    @Column(name = "subscription_code")
    private int subscriptionCode;

    @Column(name = "expenditure_content")
    private String expenditureContent;

    @Column(name = "expenses")
    private int expenses;
}
