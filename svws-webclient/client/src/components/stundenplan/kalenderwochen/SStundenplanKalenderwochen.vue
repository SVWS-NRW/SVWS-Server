<template>
	<div class="page page-flex-row select-none">
		<Teleport to=".svws-sub-nav-target" defer v-if="hatUpdateKompetenz">
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<svws-ui-button class="subNavigationFocusField" @click="modus = !modus" title="Modus wechseln" type="transparent">
					<span :class="[modus ? 'icon-sm i-ri-play-line' : 'icon-sm i-ri-speed-line']" />
					Modus: <span>{{ modus ? 'Einzeln bearbeiten':'Fortlaufend bearbeiten' }}</span>
				</svws-ui-button>
				<svws-ui-button v-if="!stundenplanManager().kalenderwochenzuordnungGetMengeUngueltige().isEmpty()" @click="deleteKalenderwochenzuordnungen" title="Ungültige Zuordnungen löschen" type="danger">
					<span class="icon-sm i-ri-delete-bin-line" /> Ungültige Zuordnungen entfernen
				</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<div class="min-w-fit max-w-280 h-full overflow-hidden">
			<div v-if="hatUpdateKompetenz">Zum Ändern der Kalenderwoche die jeweilige Zeile anklicken</div>
			<svws-ui-table :items :columns :clickable="hatUpdateKompetenz" @update:clicked="toggleWochentyp" scroll />
		</div>
		<div class="min-w-fit max-w-140 h-full overflow-auto">
			<svws-ui-table :items="summen" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { StundenplanKalenderwochenzuordnung } from "@core";
	import { ArrayList, BenutzerKompetenz, DateUtils } from "@core";
	import { useRegionSwitch } from "@ui";
	import type { StundenplanKalenderwochenProps } from "./SStundenplanKalenderwochenProps";

	const props = defineProps<StundenplanKalenderwochenProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

	async function nextWochentyp(kw: StundenplanKalenderwochenzuordnung) {
		const kalenderwochenZuordnung = new ArrayList<StundenplanKalenderwochenzuordnung>();
		const modell = props.stundenplanManager().getWochenTypModell();
		const list = modus.value ? [kw] : props.stundenplanManager().kalenderwochenzuordnungGetMengeAsList();
		for (const e of list) {
			if (e.kw >= kw.kw)
				e.wochentyp = (e.wochentyp + 1 > modell) ? 1 : e.wochentyp + 1;
			kalenderwochenZuordnung.add(e);
		}
		await props.patchKalenderwochenzuordnungen(kalenderwochenZuordnung);
	}

	async function toggleWochentyp(value: { zuordnung: StundenplanKalenderwochenzuordnung }) {
		await nextWochentyp(value.zuordnung);
	}

	const modus = ref<boolean>(true);

	const summen = computed<Array<{ wochentyp: string, anzahl: number }>>(() => {
		const result : Array<{ wochentyp: string, anzahl: number }> = [];
		for (let wt = 1; wt <= props.stundenplanManager().getWochenTypModell(); wt++)
			result.push({
				wochentyp: props.stundenplanManager().stundenplanGetWochenTypAsString(wt),
				anzahl: props.stundenplanManager().kalenderwochenzuordnungGetMengeByWochentyp(wt).size(),
			});
		return result;
	});

	const columns = [
		{ key: "kalenderwoche", label: "Kalenderwoche", span: 1 },
		{ key: "montag", label: "Montag", span: 1 },
		{ key: "sonntag", label: "Sonntag", span: 1 },
		{ key: "wochentyp", label: "Wochentyp", span: 1 },
	]

	const items = computed<Array<{ zuordnung: StundenplanKalenderwochenzuordnung, kalenderwoche: number, montag: string, sonntag: string, wochentyp: string }>>(() => {
		const result : Array<{ zuordnung: StundenplanKalenderwochenzuordnung, kalenderwoche: number, montag: string, sonntag: string, wochentyp: string }> = [];
		for (const zuordnung of props.stundenplanManager().kalenderwochenzuordnungGetMengeAsList())
			result.push({
				zuordnung: zuordnung,
				kalenderwoche: zuordnung.kw,
				montag: DateUtils.gibDatumGermanFormat(DateUtils.gibDatumDesMontagsOfJahrAndKalenderwoche(zuordnung.jahr, zuordnung.kw)),
				sonntag: DateUtils.gibDatumGermanFormat(DateUtils.gibDatumDesSonntagsOfJahrAndKalenderwoche(zuordnung.jahr, zuordnung.kw)),
				wochentyp: props.stundenplanManager().stundenplanGetWochenTypAsStringKurz(zuordnung.wochentyp),
			});
		return result;
	});

</script>
