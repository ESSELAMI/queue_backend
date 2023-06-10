package dz.me.filleattente.entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TentativeAcces {

	@Column(name = "TENTATIVE")
	private int tentative;
	@Column(name = "DATE_TENTATIVE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTentative;
	@Id
	@Basic(optional = false)
	@Column(name = "IP_REQUEST")
	private String ipRequest;

}
