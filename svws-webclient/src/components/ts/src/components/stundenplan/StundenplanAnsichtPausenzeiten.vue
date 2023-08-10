<template>
	<div class="stundenplan h-full" :class="cols">
		<div class="stundenplan-cell"> &ndash; </div>
		<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="stundenplan-cell bg-svws-300">
			<div> {{ wochentage[wochentag.id] }} </div>
		</div>
		<div class="stundenplan-cell relative">
			<div v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde" class="bg-yellow-200 absolute w-48 rounded-md"
				:style="posZeitraster(undefined, stunde)">
				<div> {{ stunde }}. Stunde </div>
				<div v-for="zeiten in manager().unterrichtsstundeGetUhrzeitenAsStrings(stunde)" :key="zeiten">
					{{ zeiten }}
				</div>
			</div>
		</div>
		<div v-for="wochentag in manager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="relative">
			<template v-for="stunde in manager().zeitrasterGetStundenRange()" :key="stunde">
				<div class="absolute h-full w-full bg-yellow-100 rounded-md" :style="posZeitraster(wochentag, stunde)">
					<div v-if="(wochentyp() !== 0) && (manager().zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag, stunde, 0) || manager().zeitrasterHatUnterrichtByWochentagAndStundeAndWochentyp(wochentag, stunde, wochentyp()))" class="h-full w-full flex flex-col gap-2">
						<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(wochentag, stunde, wochentyp(), true)" :key="unterricht.id"
							class="stundenplan-unterricht bg-cyan-100" :class="mode === 'schueler' ? 'grid-cols-2' : mode === 'lehrer' ? 'grid-cols-3' : 'grid-cols-4'">
							<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
							<div> {{ unterricht.wochentyp !== 0 ? "" + unterricht.wochentyp : "&nbsp;" }} </div>
							<div v-if="mode !== 'lehrer'"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
							<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
						</div>
					</div>
					<div v-if="(wochentyp() === 0) && manager().zeitrasterHatUnterrichtMitWochentyp0ByWochentagAndStunde(wochentag, stunde)" class="h-full w-full flex flex-col gap-2">
						<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, 0)" :key="unterricht.id"
							class="stundenplan-unterricht bg-cyan-100" :class="mode === 'schueler' ? 'grid-cols-2' : mode === 'lehrer' ? 'grid-cols-3' : 'grid-cols-4'">
							<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
							<div> &nbsp; </div>
							<div v-if="mode !== 'lehrer'"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
							<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
						</div>
					</div>
					<div v-else-if="(wochentyp() === 0) && manager().zeitrasterHatUnterrichtMitWochentyp1BisNByWochentagAndStunde(wochentag, stunde)" class="h-full w-full flex flex-row gap-2">
						<div v-for="wt in manager().getWochenTypModell()" :key="wt" class="flex flex-col gap-2 bg-cyan-100">
							<div v-for="unterricht in manager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wt)" :key="unterricht.id"
								class="stundenplan-unterricht" :class="mode === 'schueler' ? 'grid-cols-2' : mode === 'lehrer' ? 'grid-cols-3' : 'grid-cols-4'">
								<div> {{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }} </div>
								<div> {{ wt }} </div>
								<div v-if="mode !== 'lehrer'"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
								<div> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
							</div>
						</div>
					</div>
				</div>
			</template>
			<template v-for="pausenaufsicht in pausenaufsichtGetMengeByWochentagOrEmptyList(wochentag)" :key="pausenaufsicht.id">
				<div class="absolute h-full w-full stundenplan-unterricht bg-green-100" :style="posPausenaufsicht(pausenaufsicht)">
					<div> {{ pausenzeit(pausenaufsicht).bezeichnung }} </div>
					<div> &nbsp; </div>
					<div v-if="mode !== 'lehrer'"> {{ manager().lehrerGetByIdOrException(pausenaufsicht.idLehrer).kuerzel }} </div>
					<div> {{ aufsichtsbereiche(pausenaufsicht) }} </div>
				</div>
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ArrayList, type List, Wochentag, type StundenplanPausenaufsicht, type StundenplanPausenzeit, type StundenplanAufsichtsbereich } from "@core";
	import { computed } from "vue";
	import { type StundenplanAnsichtProps } from "./StundenplanAnsichtProps";

	const wochentage = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag' ];

	const props = defineProps<StundenplanAnsichtProps>();

	const beginn = computed(() => {
		// TODO Pausenzeiten Min
		return props.manager().zeitrasterGetMinutenMin();
	});

	const ende = computed(() => {
		// TODO Pausenzeiten Max
		return props.manager().zeitrasterGetMinutenMax();
	});

	const gesamtzeit = computed(() => {
		const tmp = ende.value - beginn.value;
		return tmp <= 0 ? 360 : tmp;
	});

	function pausenzeit(pausenaufsicht: StundenplanPausenaufsicht): StundenplanPausenzeit {
		return props.manager().pausenzeitGetByIdOrException(pausenaufsicht.idPausenzeit);
	}

	function aufsichtsbereiche(pausenaufsicht: StundenplanPausenaufsicht): string {
		let result = "";
		for (const idBereich of pausenaufsicht.bereiche) {
			const bereich = props.manager().aufsichtsbereichGetByIdOrException(idBereich);
			if (result !== "")
				result += ",";
			result += bereich.kuerzel;
		}
		return result;
	}

	// TODO ersetzen mit entsprechender Methode im Manager
	function pausenaufsichtGetMengeByWochentagOrEmptyList(wochentag : Wochentag): List<StundenplanPausenaufsicht> {
		const allePausenaufsichten = props.manager().pausenaufsichtGetMengeAsList();
		const result = new ArrayList<StundenplanPausenaufsicht>();
		for (const p of allePausenaufsichten) {
			if (pausenzeit(p).wochentag === wochentag.id)
				result.add(p);
		}
		return result;
	}

	function posZeitraster(wochentag: Wochentag | undefined, stunde: number): string {
		// TODO Ermittle die Info aus allen vorhanden Wochentagen (!) - nicht nur vom Montag
		const w = wochentag === undefined ? Wochentag.MONTAG : wochentag;
		const z = props.manager().zeitrasterGetByWochentagAndStundeOrException(w.id, stunde);
		let top = 0;
		let height = 10;
		if ((z.stundenbeginn === null) || (z.stundenende === null)) {
			const sb = props.manager().zeitrasterGetStundeMin();
			const se = props.manager().zeitrasterGetStundeMax();
			const stunden = se - sb + 1;
			top = ((stunde - sb) / stunden) * 100;
			height = 100 / stunden;
		} else {
			top = ((z.stundenbeginn - beginn.value) / gesamtzeit.value) * 100;
			height = ((z.stundenende - z.stundenbeginn) / gesamtzeit.value) * 100;
		}
		return "top: " + top + "%; height: " + height + "%;";
	}


	function posPausenaufsicht(p : StundenplanPausenaufsicht) : string {
		const pzeit = pausenzeit(p);
		let top = 0;
		let height = 10;
		if ((pzeit.beginn === null) || (pzeit.ende === null)) {
			const sb = props.manager().zeitrasterGetStundeMin();
			const se = props.manager().zeitrasterGetStundeMax();
			const stunden = se - sb + 1;
			top = 0;
			height = 100 / stunden;
		} else {
			top = ((pzeit.beginn - beginn.value) / gesamtzeit.value) * 100;
			height = ((pzeit.ende - pzeit.beginn) / gesamtzeit.value) * 100;
		}
		return "top: " + top + "%; height: " + height + "%;";
	}

	const cols = computed(() => {
		const colcount = props.manager().zeitrasterGetWochentageAlsEnumRange().length;
		const result = {
			'stundenplan-col1': colcount === 1,
			'stundenplan-col2': colcount === 2,
			'stundenplan-col3': colcount === 3,
			'stundenplan-col4': colcount === 4,
			'stundenplan-col5': colcount === 5,
			'stundenplan-col6': colcount === 6,
			'stundenplan-col7': colcount === 7,
		};
		return result;
	});

</script>

<style lang="postcss" scoped>

	.stundenplan {
		@apply grid gap-4 rounded-md select-none;
		grid-template-rows: 1.6rem 1fr
	}

	.stundenplan-col1 {
		grid-template-columns: 12rem repeat(1, minmax(0, 1fr))
	}

	.stundenplan-col2 {
		grid-template-columns: 12rem repeat(2, minmax(0, 1fr))
	}

	.stundenplan-col3 {
		grid-template-columns: 12rem repeat(3, minmax(0, 1fr))
	}

	.stundenplan-col4 {
		grid-template-columns: 12rem repeat(4, minmax(0, 1fr))
	}

	.stundenplan-col5 {
		grid-template-columns: 12rem repeat(5, minmax(0, 1fr))
	}

	.stundenplan-col6 {
		grid-template-columns: 12rem repeat(6, minmax(0, 1fr))
	}

	.stundenplan-col7 {
		grid-template-columns: 12rem repeat(7, minmax(0, 1fr))
	}

	.stundenplan-cell {
		@apply text-center rounded-md w-full h-full
	}

	.stundenplan-unterricht {
		@apply text-center rounded-md grid;
	}

</style>