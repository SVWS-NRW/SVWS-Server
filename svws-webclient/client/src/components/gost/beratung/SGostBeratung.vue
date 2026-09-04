<template>
	<div v-if="gostLaufbahnplanungState.valid" class="page page-flex-row max-w-480">
		<Teleport to=".svws-sub-nav-target" v-if="hatUpdateKompetenz" defer>
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<svws-ui-button :type="manager.modus === 'normal' ? 'transparent' : 'danger'" @click="manager.switchModus()" title="Modus wechseln">
					<span class="icon-sm i-ri-loop-right-line" /> Modus: <span>{{ manager.modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen />
				<svws-ui-button type="transparent" @click="manager.switchFaecherAnzeigen()"> {{ "Fächer anzeigen: " + manager.getTextFaecherAnzeigen() }} </svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" defer>
			<svws-ui-modal-hilfe> <hilfe-gost-beratung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="min-w-fit grow overflow-y-auto overflow-x-hidden">
			<s-laufbahnplanung-card-planung title="Vorlage für Schüler des Abiturjahrgangs" :hat-update-kompetenz :manager />
		</div>
		<div class="min-w-120 overflow-y-auto overflow-x-hidden flex flex-col gap-y-8 lg:gap-y-12 scrollbar-thin pr-4">
			<svws-ui-content-card v-if="istAbiturjahrgang" title="Beratungslehrer">
				<svws-ui-table :items="gostLaufbahnplanungState.beratungslehrer" :selectable="hatUpdateKompetenz" :model-value="selected" @update:model-value="selected=$event" count :columns="[{key: 'kuerzel', label: 'Kürzel', span: 0.25}, {key: 'name', label: 'Name'}]" class="svws-no-mx">
					<template #cell(name)="{ rowData: l }">
						{{ `${l.nachname}, ${l.vorname}` }}
					</template>
					<template #actions v-if="hatUpdateKompetenz">
						<svws-ui-select :model-value="undefined" @update:model-value="lehrer => lehrer && gostLaufbahnplanungState.addBeratungslehrer(lehrer.id)" headless indeterminate
							autocomplete :item-filter="lehrer_filter" :items="lehrer" removable title="Lehrkraft hinzufügen…" :item-text="l=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
						<svws-ui-button @click="gostLaufbahnplanungState.removeBeratungslehrer(selected)" type="trash" :disabled="!selected.length" />
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
			<svws-ui-content-card title="Textvorlagen" class="m-0">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input :disabled="!hatUpdateKompetenz" placeholder="Beratungsbögen" :model-value="gostLaufbahnplanungState.gostJahrgangsdaten.textBeratungsbogen"
						@change="textBeratungsbogen => patchJahrgangsdaten({ textBeratungsbogen }, gostLaufbahnplanungState.gostJahrgangsdaten.abiturjahr)" resizeable="vertical" autoresize />
					<svws-ui-textarea-input :disabled="!hatUpdateKompetenz" placeholder="Mailversand" :model-value="gostLaufbahnplanungState.gostJahrgangsdaten.textMailversand"
						@change="textMailversand => patchJahrgangsdaten({ textMailversand }, gostLaufbahnplanungState.gostJahrgangsdaten.abiturjahr)" resizeable="vertical" autoresize />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<s-laufbahnplanung-card-status />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBeratungslehrer } from "@core/core/data/gost/GostBeratungslehrer";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { BenutzerTyp } from "@core/core/types/benutzer/BenutzerTyp";
	import { LaufbahnplanungUiManager } from "@ui/components/gost/laufbahnplanung/LaufbahnplanungUiManager";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useConfigState } from "@ui/states/ConfigState";
	import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";
	import { useServerState } from "@ui/states/ServerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { computed, ref } from "vue";
	import type { GostBeratungProps } from "./SGostBeratungProps";
	import { lehrer_filter } from "~/utils/helfer";

	const props = defineProps<GostBeratungProps>();
	const serverState = useServerState();
	const configState = useConfigState();
	const benutzerState = useBenutzerState();
	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const manager = computed<LaufbahnplanungUiManager>(() => new LaufbahnplanungUiManager(
		serverState.mode,
		() => configState.config,
		{ faecherZeigen: "app.gost.beratung.faecher.anzeigen", modus: "app.gost.beratung.modus" },
		true,
		true
	));

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const selected = ref<GostBeratungslehrer[]>([]);

	const hatUpdateKompetenz = computed<boolean>(() => {
		let beratungslehrer = false;
		for (const b of gostLaufbahnplanungState.beratungslehrer) {
			if (b.id === benutzerState.benutzerdaten.id) {
				beratungslehrer = true;
				break;
			}
		}
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
				&& benutzerState.benutzerdaten.typ === BenutzerTyp.LEHRER.id && beratungslehrer);
	});

	const istAbiturjahrgang = computed<boolean>(() => (gostLaufbahnplanungState.valid &&
		gostLaufbahnplanungState.gostJahrgangsdaten.abiturjahr > 0));

	const lehrer = computed<Map<number, LehrerListeEintrag>>(() => {
		const map = new Map<number, LehrerListeEintrag>(gostLaufbahnplanungState.mapLehrer);
		for (const l of gostLaufbahnplanungState.beratungslehrer) {
			map.delete(l.id);
		}
		return map;
	});

</script>

<style scoped>

	.scrollbar-thin {
		scrollbar-gutter: stable;
		scrollbar-width: thin;
		scrollbar-color: rgba(0,0,0,0.2) transparent;
	}

</style>
