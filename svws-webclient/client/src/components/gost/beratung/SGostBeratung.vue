<template>
	<div v-if="jahrgangsdaten() !== undefined" class="page page-flex-row max-w-480">
		<Teleport to=".svws-sub-nav-target" v-if="hatUpdateKompetenz" defer>
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<svws-ui-button class="subNavigationFocusField" :type="modus === 'normal' ? 'transparent' : 'danger'" @click="switchModus" title="Modus wechseln">
					<span class="icon-sm i-ri-loop-right-line" />
					Modus: <span>{{ modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen :gost-jahrgangsdaten="jahrgangsdaten()" :reset-fachwahlen />
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" defer>
			<svws-ui-modal-hilfe> <hilfe-gost-beratung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="min-w-fit grow overflow-y-auto overflow-x-hidden">
			<s-laufbahnplanung-card-planung title="Vorlage für Schüler des Abiturjahrgangs" :goto-kursblockung :hat-update-kompetenz
				:manager :abiturdaten-manager :modus :faecher-anzeigen="'alle'" :gost-jahrgangsdaten="jahrgangsdaten()" :set-wahl ignoriere-sprachenfolge />
		</div>
		<div class="min-w-120 overflow-y-auto overflow-x-hidden flex flex-col gap-y-8 lg:gap-y-12 scrollbar-thin pr-4">
			<svws-ui-content-card v-if="istAbiturjahrgang" title="Beratungslehrer">
				<svws-ui-table :items="beratungslehrer()" :selectable="hatUpdateKompetenz" :model-value="selected" @update:model-value="selected=$event" count :columns="[{key: 'kuerzel', label: 'Kürzel', span: 0.25}, {key: 'name', label: 'Name'}]" class="svws-no-mx">
					<template #cell(name)="{ rowData: l }">
						{{ `${l.nachname}, ${l.vorname}` }}
					</template>
					<template #actions v-if="hatUpdateKompetenz">
						<svws-ui-select :model-value="undefined" @update:model-value="lehrer => lehrer && addBeratungslehrer(lehrer.id)" headless indeterminate
							autocomplete :item-filter="lehrer_filter" :items="lehrer" removable title="Lehrkraft hinzufügen…" :item-text="l=> `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
						<svws-ui-button @click="removeBeratungslehrer(selected)" type="trash" :disabled="!selected.length" />
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
			<svws-ui-content-card title="Textvorlagen" class="m-0">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input :disabled="!hatUpdateKompetenz" placeholder="Beratungsbögen" :model-value="jahrgangsdaten().textBeratungsbogen"
						@change="textBeratungsbogen => patchJahrgangsdaten({ textBeratungsbogen }, props.jahrgangsdaten().abiturjahr)" resizeable="vertical" autoresize />
					<svws-ui-textarea-input :disabled="!hatUpdateKompetenz" placeholder="Mailversand" :model-value="jahrgangsdaten().textMailversand"
						@change="textMailversand => patchJahrgangsdaten({ textMailversand }, props.jahrgangsdaten().abiturjahr)" resizeable="vertical" autoresize />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<s-laufbahnplanung-card-status :abiturdaten-manager :fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art :set-gost-belegpruefungs-art />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { GostBeratungProps } from "./SGostBeratungProps";
	import { BenutzerKompetenz, BenutzerTyp, type GostBeratungslehrer, type LehrerListeEintrag } from "@core";
	import { computed, ref } from "vue";
	import { lehrer_filter } from '~/utils/helfer';
	import { useRegionSwitch } from "~/components/useRegionSwitch";
	import { LaufbahnplanungUiManager } from "@ui";

	const props = defineProps<GostBeratungProps>();

	const manager = computed<LaufbahnplanungUiManager>(() => new LaufbahnplanungUiManager(props.abiturdatenManager));

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const selected = ref<GostBeratungslehrer[]>([]);

	const hatUpdateKompetenz = computed<boolean>(() => {
		let beratungslehrer = false;
		for (const b of props.beratungslehrer())
			if (b.id === props.benutzerdaten.id) {
				beratungslehrer = true;
				break;
			}
		return props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
				&& props.benutzerdaten.typ === BenutzerTyp.LEHRER.id && beratungslehrer);
	});

	const istAbiturjahrgang = computed<boolean>(() => (props.jahrgangsdaten().abiturjahr > 0));

	const modus = ref<'manuell' | 'normal' | 'hochschreiben'>('normal');

	function switchModus() {
		switch (modus.value) {
			case 'normal':
				modus.value = 'hochschreiben';
				break;
			case 'hochschreiben':
				modus.value = 'manuell';
				break;
			case 'manuell':
				modus.value = 'normal';
				break;
		}
	}

	const lehrer = computed<Map<number, LehrerListeEintrag>>(() => {
		const map = new Map<number, LehrerListeEintrag>(props.mapLehrer);
		for (const l of props.beratungslehrer())
			map.delete(l.id);
		return map;
	})

</script>

<style scoped>

	.scrollbar-thin {
		scrollbar-gutter: stable;
		scrollbar-width: thin;
		scrollbar-color: rgba(0,0,0,0.2) transparent;
	}

</style>
