<template>
	<div class="page--content page--content--full page--content--gost-grid" :class="{'svws-blockungstabelle-hidden': !blockungstabelleVisible}">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button-select v-if="hatBlockung" type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <i-ri-printer-line /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-kursplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<template v-if="hatBlockung">
			<Teleport to=".svws-sub-nav-target" v-if="isMounted">
				<svws-ui-sub-nav>
					<svws-ui-button type="transparent" @click="toggleBlockungstabelle">
						<template v-if="blockungstabelleVisible">
							<i-ri-menu-fold-line />
							Tabelle ausblenden
						</template>
						<template v-else>
							<i-ri-menu-unfold-line />
							Tabelle einblenden
						</template>
					</svws-ui-button>
					<div class="font-bold ml-8">Ergebnis:</div>
					<svws-ui-button type="transparent" @click.stop="ergebnisAbleiten()" title="Eine neue Blockung auf Grundlage dieses Ergebnisses erstellen." class="text-black dark:text-white">
						<i-ri-stackshare-line /> Ableiten
					</svws-ui-button>
					<s-card-gost-kursansicht-blockung-aktivieren-modal v-if="!persistiert" :get-datenmanager="getDatenmanager" :ergebnis-aktivieren="ergebnisAktivieren" :blockungsname="blockungsname" v-slot="{ openModal }">
						<svws-ui-button type="transparent" :disabled="!aktivieren_moeglich" size="small" @click="openModal()" title="Aktiviert die Blockung und persistierte diese in der Kurstabelle und den Leistungsdaten">
							<i-ri-pulse-line /> Aktivieren
						</svws-ui-button>
					</s-card-gost-kursansicht-blockung-aktivieren-modal>
					<s-card-gost-kursansicht-ergebnis-synchronisieren-modal v-else :get-datenmanager="getDatenmanager" :ergebnis-synchronisieren="ergebnisSynchronisieren" :blockungsname="blockungsname" v-slot="{ openModal }">
						<svws-ui-button type="transparent" :disabled="!synchronisieren_moeglich" size="small" @click="openModal()" title="Synchronisiert die Daten dieser Blockung mit den in der Kurstabelle und den Leistungsdaten persistierten Daten">
							<i-ri-loop-left-line /> Synchronisieren
						</svws-ui-button>
					</s-card-gost-kursansicht-ergebnis-synchronisieren-modal>
					<s-card-gost-kursansicht-blockung-hochschreiben-modal :get-datenmanager="getDatenmanager" :ergebnis-hochschreiben="ergebnisHochschreiben" v-slot="{ openModal }">
						<svws-ui-button type="transparent" @click="openModal()" title="Überträgt die Daten dieser Blockung in das nächste Halbjahr">
							<i-ri-corner-up-right-line /> Hochschreiben
						</svws-ui-button>
					</s-card-gost-kursansicht-blockung-hochschreiben-modal>
					<div class="font-bold ml-8">Regeln: </div>
					<svws-ui-button @click="onToggle" size="small" type="transparent" title="Alle Regeln anzeigen"
						:disabled="(regelzahl < 1) && (getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() > 1)" :class="{'mr-2': regelzahl > 0}">
						<i-ri-settings3-line /> anzeigen
						<template #badge v-if="regelzahl"> {{ regelzahl }} </template>
					</svws-ui-button>
					<svws-ui-button-select v-if="allowRegeln" type="transparent" :dropdown-actions="actionsRegeln" :default-action="{ text: 'Fixieren...', action: () => {} }" no-default />
				</svws-ui-sub-nav>
			</Teleport>
			<s-card-gost-kursansicht :config="config" :halbjahr="halbjahr" :faecher-manager="faecherManager" :hat-ergebnis="hatErgebnis"
				:get-datenmanager="getDatenmanager" :get-kursauswahl="getKursauswahl" :get-ergebnismanager="getErgebnismanager"
				:map-fachwahl-statistik="mapFachwahlStatistik" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter" :kurssortierung="kurssortierung"
				:add-regel="addRegel" :remove-regel="removeRegel" :update-kurs-schienen-zuordnung="updateKursSchienenZuordnung"
				:patch-kurs="patchKurs" :add-kurs="addKurs" :remove-kurs="removeKurs" :add-kurs-lehrer="addKursLehrer"
				:patch-schiene="patchSchiene" :add-schiene="addSchiene" :remove-schiene="removeSchiene"
				:remove-kurs-lehrer="removeKursLehrer" :ergebnis-aktivieren="ergebnisAktivieren" :existiert-schuljahresabschnitt="existiertSchuljahresabschnitt"
				:ergebnis-hochschreiben="ergebnisHochschreiben"
				:toggle-blockungstabelle="toggleBlockungstabelle"
				:blockungstabelle-visible="blockungstabelleVisible"
				:add-schiene-kurs="addSchieneKurs" :remove-schiene-kurs="removeSchieneKurs" :combine-kurs="combineKurs" :split-kurs="splitKurs" />
			<router-view name="gost_kursplanung_schueler_auswahl" />
			<router-view />
			<Teleport to="body">
				<aside class="app-layout--aside max-w-2xl h-auto" v-if="!collapsed && !(regelzahl < 1 && getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() > 1)">
					<div class="app-layout--aside-container relative h-auto max-h-full">
						<h2 class="text-headline-md flex justify-between">
							<span>Regeln zur Blockung</span>
							<svws-ui-button type="transparent" @click="onToggle"> Schließen </svws-ui-button>
						</h2>
						<s-card-gost-regelansicht :get-datenmanager="getDatenmanager" :faecher-manager="faecherManager" :map-schueler="mapSchueler"
							:patch-regel="patchRegel" :add-regel="addRegel" :remove-regel="removeRegel" />
					</div>
				</aside>
			</Teleport>
		</template>
		<div v-else class="col-span-full">
			<div class="p-3 border-2 border-dashed border-black/10 dark:border-white/10 rounded-lg max-w-xl">
				<div class="text-headline-md mb-5">Keine Blockung ausgewählt</div>
				<div class="opacity-50 leading-tight flex flex-col gap-2">
					<div>Klicke in der Übersicht auf <i-ri-add-line class="inline -mt-1" />, um eine neue Kursplanung zu erstellen.</div>
					<div>Falls eine persistierte Blockung vorliegt, kann mit "Wiederherstellen" aus bestehenden Leistungsdaten eine Blockung geladen werden.</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { GostKursplanungProps } from "./SGostKursplanungProps";
	import { computed, ref, onMounted } from "vue";

	const props = defineProps<GostKursplanungProps>();

	const collapsed = ref<boolean>(true);

	const regelzahl = computed<number>(() => props.hatBlockung ? props.getDatenmanager().regelGetAnzahl() : 0);

	const blockungsname = computed<string>(() => props.getDatenmanager().daten().name);

	const bereits_aktiv = computed<boolean>(() => props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id]);

	const allowRegeln = computed<boolean>(() => (props.getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() === 1));

	const vergangenheit = computed<boolean>(()=> props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id+1]);
	const persistiert = computed<boolean>(()=> props.jahrgangsdaten().istBlockungFestgelegt[props.halbjahr.id])
	const aktivieren_moeglich = computed<boolean>(() => !vergangenheit.value && !persistiert.value);
	const synchronisieren_moeglich = computed<boolean>(()=> !vergangenheit.value && persistiert.value);

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const blockungstabelleVisible = ref(true);

	const onToggle = () => collapsed.value = !collapsed.value;
	const toggleBlockungstabelle = () => blockungstabelleVisible.value = !blockungstabelleVisible.value;

	const dropdownList = [
		{ text: "Schülerliste markierte Kurse", action: () => downloadPDF("Schülerliste markierte Kurse"), default: true },
		{ text: "Kurse-Schienen-Zuordnung", action: () => downloadPDF("Kurse-Schienen-Zuordnung") },
		{ text: "Kurse-Schienen-Zuordnung markierter Schüler", action: () => downloadPDF("Kurse-Schienen-Zuordnung markierter Schüler") },
		{ text: "Kurse-Schienen-Zuordnung gefilterte Schüler", action: () => downloadPDF("Kurse-Schienen-Zuordnung gefilterte Schüler") },
		{ text: "Kursbelegung markierter Schüler", action: () => downloadPDF("Kursbelegung markierter Schüler") },
		{ text: "Kursbelegung gefilterte Schüler", action: () => downloadPDF("Kursbelegung gefilterte Schüler") },
	]

	async function downloadPDF(title: string) {
		const { data, name } = await props.getPDF(title);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	const actionsRegeln = computed(() => {
		const kursauswahl = props.getKursauswahl();
		const allSelected = (props.getDatenmanager().kursGetAnzahl() === kursauswahl.size);
		const hatAbiturkurse = (props.halbjahr.halbjahr > 1);
		const result: Array<{ text: string; action: () => Promise<void>; default?: boolean; }> = [];
		result.push({ text: "Fixiere alle Kurse", action: async () => await props.updateRegeln("fixiereKurseAlle") });
		result.push({ text: "Löse alle fixierten Kurse", action: async () => await props.updateRegeln("loeseKurseAlle") });
		if ((props.getKursauswahl().size === 0) || allSelected) {
			result.push({ text: "Fixiere alle Schüler", action: async () => await props.updateRegeln("fixiereSchuelerAlle") });
			if (hatAbiturkurse)
				result.push({ text: "Fixiere alle Schüler mit Abiturkursen", action: async () => await props.updateRegeln("fixiereSchuelerAbiturkurseAlle") });
			result.push({ text: "Löse alle fixierten Schüler", action: async () => await props.updateRegeln("loeseSchuelerAlle") });
		} else {
			result.push({ text: "Kursauswahl: Fixiere Kurse", action: async () => await props.updateRegeln("fixiereKursauswahl") });
			result.push({ text: "Kursauswahl: Löse fixierte Kurse", action: async () => await props.updateRegeln("loeseKursauswahl") });
			result.push({ text: "Kursauswahl: Fixiere Schüler", action: async () => await props.updateRegeln("fixiereSchuelerKursauswahl") });
			if (hatAbiturkurse)
				result.push({ text: "Kursauswahl: Fixiere Schüler mit Abiturkursen", action: async () => await props.updateRegeln("fixiereSchuelerAbiturkurseKursauswahl") });
			result.push({ text: "Kursauswahl: Löse fixierte Schüler", action: async () => await props.updateRegeln("loeseSchuelerKursauswahl") });
		}
		return result;
	});

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 lg:gap-x-12;
		grid-auto-rows: 100%;
		grid-template-columns: minmax(min-content, 1.5fr) minmax(18rem, 0.4fr) 1fr;
		grid-auto-columns: max-content;

		&.svws-blockungstabelle-hidden {
			grid-template-columns: 0 minmax(20rem, 0.15fr) 1fr;

			.s-gost-kursplanung-schueler-auswahl {
				@apply -ml-8 lg:-ml-12;
			}
		}
	}

</style>
