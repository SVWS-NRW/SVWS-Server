package de.svws_nrw.service.schueler.stammdaten;

import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schule.kataloge.fahrschuelerart.FahrschuelerartRepository;
import de.svws_nrw.repo.schule.kataloge.haltestelle.HaltestelleRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.kataloge.religion.ReligionRepository;

public record SchuelerStammdatenRepositories(
		SchuelerRepository schuelerRepository,
		ReligionRepository religionRepository,
		OrtRepository ortRepository,
		OrtsteilRepository ortsteilRepository,
		FahrschuelerartRepository fahrschuelerartRepository,
		HaltestelleRepository haltestelleRepository
) { }
