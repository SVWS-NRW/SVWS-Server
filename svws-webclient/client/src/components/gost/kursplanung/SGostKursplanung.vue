<template>
	<div class="page--content page--content--full page--content--gost-grid" :class="{'svws-blockungstabelle-hidden': !blockungstabelleVisible}">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button v-if="hatBlockung" type="secondary" @click.prevent="downloadPDFKursSchienenZuordnung" title="Kurs-Schienen-Zuordnung herunterladen">
				<i-ri-printer-line />Kurs-Schienen-Zuordnung
			</svws-ui-button>
			<svws-ui-modal-hilfe> <hilfe-kursplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<template v-if="hatBlockung">
			<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
				<svws-ui-sub-nav>
					<svws-ui-button @click="onToggle" size="small" type="transparent" :disabled="regelzahl < 1 && getDatenmanager().ergebnisGetListeSortiertNachBewertung().size() > 1" :class="{'mr-2': regelzahl > 0}">
						<i-ri-settings3-line />
						<span class="pr-1">Regeln zur Blockung</span>
						<template #badge v-if="regelzahl"> {{ regelzahl }} </template>
					</svws-ui-button>
					<s-card-gost-kursansicht-blockung-aktivieren-modal v-if="!persistiert" :get-datenmanager="getDatenmanager" :ergebnis-aktivieren="ergebnisAktivieren" :blockungsname="blockungsname" v-slot="{ openModal }">
						<template v-if="aktivieren_moeglich">
							<svws-ui-button type="transparent" size="small" @click="openModal()">Aktivieren</svws-ui-button>
						</template>
						<template v-else>
							<svws-ui-tooltip>
								<svws-ui-button disabled type="transparent" size="small">Aktivieren</svws-ui-button>
								<template #content>
									<span v-if="!existiertSchuljahresabschnitt"> Die Blockung kann nicht aktiviert werden, da noch kein Abschnitt für dieses Halbjahr angelegt ist. </span>
									<span v-if="bereits_aktiv"> Die Blockung kann nicht aktiviert werden, da bereits Kurse der gymnasialen Oberstufe für diesen Abschnitt angelegt sind. </span>
									<span v-else />
								</template>
							</svws-ui-tooltip>
						</template>
					</s-card-gost-kursansicht-blockung-aktivieren-modal>
					<s-card-gost-kursansicht-ergebnis-synchronisieren-modal v-else :get-datenmanager="getDatenmanager" :ergebnis-synchronisieren="ergebnisSynchronisieren" :blockungsname="blockungsname" v-slot="{ openModal }">
						<template v-if="synchronisieren_moeglich">
							<svws-ui-button type="transparent" size="small" @click="openModal()">Synchronisieren</svws-ui-button>
						</template>
						<template v-else>
							<svws-ui-tooltip>
								<svws-ui-button disabled type="transparent" size="small">Synchronisieren</svws-ui-button>
								<template #content>
									<span>Nur aktuelle und zukünftige, bereits aktivierte und persistierte Blockungen können synchronisiert werden</span>
								</template>
							</svws-ui-tooltip>
						</template>
					</s-card-gost-kursansicht-ergebnis-synchronisieren-modal>
					<s-card-gost-kursansicht-blockung-hochschreiben-modal :get-datenmanager="getDatenmanager" :ergebnis-hochschreiben="ergebnisHochschreiben" v-slot="{ openModal }">
						<svws-ui-button type="transparent" @click="openModal()">Ergebnis hochschreiben</svws-ui-button>
					</s-card-gost-kursansicht-blockung-hochschreiben-modal>
					<svws-ui-button type="transparent" @click="toggleBlockungstabelle" class="ml-auto">
						<template v-if="blockungstabelleVisible">
							<i-ri-menu-fold-line />
							Kursplanung ausblenden
						</template>
						<template v-else>
							<i-ri-menu-unfold-line />
							Kursplanung einblenden
						</template>
					</svws-ui-button>
				</svws-ui-sub-nav>
			</Teleport>
			<s-card-gost-kursansicht :config="config" :halbjahr="halbjahr" :faecher-manager="faecherManager" :hat-ergebnis="hatErgebnis"
				:jahrgangsdaten="jahrgangsdaten"
				:get-datenmanager="getDatenmanager" :get-ergebnismanager="getErgebnismanager"
				:map-fachwahl-statistik="mapFachwahlStatistik" :map-lehrer="mapLehrer" :schueler-filter="schuelerFilter"
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
						<h2 class="text-headline-md flex justify-between px-3">
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

	const bereits_aktiv = computed<boolean>(() => props.jahrgangsdaten.istBlockungFestgelegt[props.halbjahr.id]);

	const vergangenheit = computed<boolean>(()=> props.jahrgangsdaten.istBlockungFestgelegt[props.halbjahr.id+1]);
	const persistiert = computed<boolean>(()=> props.jahrgangsdaten.istBlockungFestgelegt[props.halbjahr.id])
	const aktivieren_moeglich = computed<boolean>(() => !vergangenheit.value && !persistiert.value);
	const synchronisieren_moeglich = computed<boolean>(()=> !vergangenheit.value && persistiert.value);

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const blockungstabelleVisible = ref(true);

	const onToggle = () => collapsed.value = !collapsed.value;
	const toggleBlockungstabelle = () => blockungstabelleVisible.value = !blockungstabelleVisible.value;

	async function downloadPDFKursSchienenZuordnung() {
		const { data, name } = await props.getPDFKursSchienenZuordnung();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply grid overflow-y-hidden overflow-x-auto h-full pb-8;
		grid-auto-rows: 100%;
		grid-template-columns: minmax(min-content, 1.5fr) minmax(18rem, 0.4fr) 1fr;
		grid-auto-columns: max-content;

		&.svws-blockungstabelle-hidden {
			grid-template-columns: 0 minmax(20rem, 0.15fr) 1fr;

			.s-gost-kursplanung-schueler-auswahl {
			@apply -ml-8 lg:-ml-16;
			}
		}
	}

</style>
