<template>
	<div class="flex flex-col w-full h-full overflow-hidden">
		<div class="page page-grid-cards">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-content-card title="Ganztag">
					<svws-ui-input-wrapper :grid="1">
						<ui-select v-if="showGebundenerGanztag"
							label="Form des gebundenen Ganztags"
							v-model="gebundenerGanztag"
							:manager="formGebundenerGanztagManager"
							required statistics
							:removable="false" :readonly />
						<svws-ui-spacing :size="1" />
						<svws-ui-checkbox v-if="showOffenerGanztag" :readonly v-model="istOffenerGanztag" statistics>
							Offener Ganztag
						</svws-ui-checkbox>
						<svws-ui-spacing />
						<ui-select v-if="showOffenerGanztag"
							label="Form des offenen Ganztags"
							v-model="offenerGanztag"
							:manager="formOffenerGanztagManager"
							required statistics
							:removable="false" :readonly />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-content-card title="Weitere Merkmale">
					<svws-ui-input-wrapper :grid="1">
						<ui-select v-if="showBilingualerUnterricht"
							label="Form biligunaler Unterricht"
							v-model="bilingualerUnterricht"
							:manager="formBilingualerUnterrichtManager"
							required statistics
							:removable="false" :readonly />
						<svws-ui-spacing :size="1" />
						<svws-ui-checkbox v-if="schuleState.schulform === Schulform.R" :readonly v-model="hatRealschuleHauptschulbildungsgang" statistics>
							<svws-ui-tooltip>
								Hat Hauptschulbildungsgang
								<template #content>
									hat Hauptschulbildungsgang nach §15 Abs. 5 SchulG
								</template>
							</svws-ui-tooltip>
						</svws-ui-checkbox>
						<svws-ui-checkbox v-if="showKonfessionelleKooperation" :readonly v-model="hatKonfessionelleKooperation" statistics>
							<svws-ui-tooltip>
								Konfessionelle Kooperation
								<template #content>
									Konfessionelle Kooperation im Religionsunterricht nach BASS 12-05 Nr. 1 Nr. 6
								</template>
							</svws-ui-tooltip>
						</svws-ui-checkbox>
						<svws-ui-spacing />
						<ui-select v-if="showTalentschule"
							label="Talentschule"
							v-model="talentschule"
							:manager="formTalentschuleManager"
							required statistics
							:removable="false" :readonly />
						<svws-ui-spacing />
						<ui-select v-if="showReformpaedagogik"
							label="Reformpädagogik"
							v-model="reformpaedagogik"
							:manager="formReformpaedagogikManager"
							required statistics
							:removable="false" :readonly />
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
				<svws-ui-content-card title="Angabe für die Amtlichen Schuldaten">
					<svws-ui-input-wrapper :grid="1">
						<svws-ui-checkbox v-if="showInternationaleKontakte" :readonly v-model="hatInternationaleKontakte" statistics>
							Hat Internationale Kontakte
						</svws-ui-checkbox>
						<svws-ui-checkbox v-if="showJVA" :readonly v-model="istJVA" statistics>
							JVA-Schule
						</svws-ui-checkbox>
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
			</svws-ui-input-wrapper>
		</div>
	</div>
</template>

<script setup lang="ts">


	import { computed } from "vue";

	import { FormBilingualerUnterricht } from "@core/asd/types/schule/FormBilingualerUnterricht";
	import { FormGebundenerGanztag } from "@core/asd/types/schule/FormGebundenerGanztag";
	import { FormOffenerGanztag } from "@core/asd/types/schule/FormOffenerGanztag";
	import { Reformpaedagogik } from "@core/asd/types/schule/Reformpaedagogik";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { Talentschule } from "@core/asd/types/schule/Talentschule";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";

	import type { SchuleMerkmaleProps } from "./SchuleMerkmaleProps";


	const props = defineProps<SchuleMerkmaleProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));

	const showGebundenerGanztag = [Schulform.G, Schulform.H, Schulform.V, Schulform.S, Schulform.R, Schulform.PS, Schulform.SK, Schulform.GE, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GY, Schulform.KS, Schulform.SR, Schulform.SG]
		.includes(schuleState.schulform);
	const showOffenerGanztag = [Schulform.G, Schulform.V, Schulform.S, Schulform.PS, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.KS]
		.includes(schuleState.schulform);
	const showBilingualerUnterricht = [Schulform.G, Schulform.R, Schulform.PS, Schulform.SK, Schulform.GE, Schulform.GY, Schulform.BK, Schulform.SR, Schulform.SG]
		.includes(schuleState.schulform);
	const showKonfessionelleKooperation = [Schulform.G, Schulform.H, Schulform.V, Schulform.S, Schulform.R, Schulform.PS, Schulform.SK, Schulform.GE, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GY, Schulform.WB, Schulform.BK, Schulform.KS, Schulform.SR, Schulform.SG, Schulform.SB]
		.includes(schuleState.schulform);
	const showInternationaleKontakte = [Schulform.G, Schulform.H, Schulform.V, Schulform.S, Schulform.R, Schulform.PS, Schulform.SK, Schulform.GE, Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GY, Schulform.WB, Schulform.BK, Schulform.KS, Schulform.SR, Schulform.SG, Schulform.SB]
		.includes(schuleState.schulform);
	const showReformpaedagogik = [Schulform.G, Schulform.H, Schulform.V, Schulform.S, Schulform.R, Schulform.PS, Schulform.SK, Schulform.GE, Schulform.GY, Schulform.KS, Schulform.SR, Schulform.SG]
		.includes(schuleState.schulform);
	const showTalentschule = [Schulform.H, Schulform.R, Schulform.SK, Schulform.GE, Schulform.GY, Schulform.BK]
		.includes(schuleState.schulform);
	const showJVA = [Schulform.BK, Schulform.SB]
		.includes(schuleState.schulform);
	const formGebundenerGanztagManager = new CoreTypeSelectManager({
		clazz: FormGebundenerGanztag.class,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});
	const formOffenerGanztagManager = new CoreTypeSelectManager({
		clazz: FormOffenerGanztag.class,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});
	const formBilingualerUnterrichtManager = new CoreTypeSelectManager({
		clazz: FormBilingualerUnterricht.class,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});
	const formTalentschuleManager = new CoreTypeSelectManager({
		clazz: Talentschule.class,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});
	const formReformpaedagogikManager = new CoreTypeSelectManager({
		clazz: Reformpaedagogik.class,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const talentschule = computed({
		get: () => Talentschule.data().getEintragByID(schuleState.stammdaten.idTalentschule),
		set: (eintrag) => void props.patch({ idTalentschule: eintrag?.id ?? 0 }),
	});
	const reformpaedagogik = computed({
		get: () => Reformpaedagogik.data().getEintragByID(schuleState.stammdaten.idReformpaedagogik),
		set: (eintrag) => void props.patch({ idReformpaedagogik: eintrag?.id ?? 0 }),
	});
	const bilingualerUnterricht = computed({
		get: () => FormBilingualerUnterricht.data().getEintragByID(schuleState.stammdaten.idBilingualerUnterricht),
		set: (eintrag) => void props.patch({ idBilingualerUnterricht: eintrag?.id ?? 0 }),
	});
	const gebundenerGanztag = computed({
		get: () => FormGebundenerGanztag.data().getEintragByID(schuleState.stammdaten.idGebundenerGanztag),
		set: (eintrag) => void props.patch({ idGebundenerGanztag: eintrag?.id ?? 0 }),
	});
	const offenerGanztag = computed({
		get: () => FormOffenerGanztag.data().getEintragByID(schuleState.stammdaten.idFormOffenerGanztag),
		set: (eintrag) => void props.patch({ idFormOffenerGanztag: eintrag?.id ?? 0 }),
	});
	const istOffenerGanztag = computed({
		get: () => schuleState.stammdaten.istOffenerGanztag,
		set: (istOffenerGanztag) => void props.patch({ istOffenerGanztag }),
	});
	const hatRealschuleHauptschulbildungsgang = computed({
		get: () => schuleState.stammdaten.hatRealschuleHauptschulbildungsgang,
		set: (hatRealschuleHauptschulbildungsgang) => void props.patch({ hatRealschuleHauptschulbildungsgang }),
	});
	const hatKonfessionelleKooperation = computed({
		get: () => schuleState.stammdaten.hatKonfessionelleKooperation,
		set: (hatKonfessionelleKooperation) => void props.patch({ hatKonfessionelleKooperation }),
	});
	const hatInternationaleKontakte = computed({
		get: () => schuleState.stammdaten.hatInternationaleKontakte,
		set: (hatInternationaleKontakte) => void props.patch({ hatInternationaleKontakte }),
	});
	const istJVA = computed({
		get: () => schuleState.stammdaten.istJva,
		set: (istJva) => void props.patch({ istJva }),
	});
</script>
