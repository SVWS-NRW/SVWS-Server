<template>
	<svws-ui-notification v-if="lehrerFehler || lehrerDrafts.size > 0" type="error">
		{{ lehrerFehler }}
		<svws-ui-button v-if="gueltigeLehrerPatches.length > 0"
			type="secondary"
			:disabled="loading"
			@click="lehrerEntwuerfeSpeichern">
			Gültige Lehrkraftänderungen erneut speichern
		</svws-ui-button>
		<svws-ui-button v-if="lehrerDrafts.size > 0"
			type="secondary"
			:disabled="loading"
			@click="lehrerEntwuerfeVerwerfen">
			Lehrkraftänderungen verwerfen
		</svws-ui-button>
	</svws-ui-notification>
	<svws-ui-content-card title="Lehrkräfte der Lerngruppe"
		class="uv-deaktivierbar col-span-full"
		:inert="bearbeitung !== null && bearbeitung.art !== 'lerngruppe-lehrer'">
		<template #actions v-if="hatKompetenzAendern"><svws-ui-button v-if="bearbeitung === null" type="secondary" @click="bearbeiten('lerngruppe-lehrer', lerngruppe.id)">Bearbeiten</svws-ui-button></template>
		<div v-if="lerngruppenlehrer.size() > 0" class="grid grid-cols-[repeat(auto-fill,minmax(13rem,1fr))] gap-3">
			<s-uv-planungs-lehrer-zuordnung-kachel v-for="zuordnung in lerngruppenlehrer"
				:key="zuordnung.id"
				:id-lerngruppe="lerngruppe.id"
				:lehrer="state.uvManager.lehrerGetByLerngruppenLehrer(zuordnung)"
				:reihenfolge="lehrerDrafts.get(zuordnung.id)?.reihenfolge ?? zuordnung.reihenfolge"
				:wochenstunden="lehrerDrafts.get(zuordnung.id)?.wochenstunden ?? zuordnung.wochenstunden"
				:max-stunden="lerngruppe.wochenstundenUnterrichtet"
				:readonly="!hatKompetenzAendern"
				:loading
				:editing="bearbeitung?.art === 'lerngruppe-lehrer'"
				:oeffnen="() => gotoLehrer(zuordnung.idLehrer)"
				:reihenfolge-aendern="value => patchLehrer(zuordnung, { reihenfolge: value ?? -1 })"
				:stunden-aendern="value => patchLehrer(zuordnung, { wochenstunden: value ?? -1 })"
				:entfernen="() => removeLerngruppenLehrer(zuordnung)" />
		</div>
		<p v-else class="text-sm text-ui-secondary">Keine Lehrkräfte zugeordnet.</p>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from "vue";

	import type { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
	import type { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
	import { useUvState } from "@ui/states/UvState";

	import SUvPlanungsLehrerZuordnungKachel from '../SUvPlanungsLehrerZuordnungKachel.vue';
	import type { UvZuordnungArt, UvZuordnungBearbeitung } from '../UvPlanung';
	import { useUvPlanungsManager } from '../UvPlanungsKontext';

	const { lerngruppe, bearbeitung } = defineProps<{
		lerngruppe: UvLerngruppe;
		bearbeitung: UvZuordnungBearbeitung | null;
		bearbeiten: (art: UvZuordnungArt, idZiel: number) => void;
		gotoLehrer: (id: number) => void;
	}>();

	const loading = defineModel<boolean>('loading', { required: true });
	const state = useUvState();
	const planungsManager = useUvPlanungsManager();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const lehrerFehler = ref('');
	const lehrerDrafts = shallowRef(new Map<number, Partial<UvLerngruppenLehrer>>());

	watch(() => lerngruppe.id, lehrerEntwuerfeVerwerfen);

	function lehrerEntwuerfeVerwerfen(): void {
		lehrerDrafts.value = new Map();
		lehrerFehler.value = '';
	}

	// Entwürfe für Lehrkraftzuordnungen prüfen und gemeinsam speichern

	function lehrerEntwurfGueltig(row: UvLerngruppenLehrer, data: Partial<UvLerngruppenLehrer>): boolean {
		return planungsManager.value?.lehrerZuordnungGueltig(lerngruppe, row.idLehrer, row.id,
			data.reihenfolge ?? row.reihenfolge, data.wochenstunden ?? row.wochenstunden,
			data.wochenstundenAngerechnet ?? row.wochenstundenAngerechnet) === true;
	}

	async function patchLehrer(row: UvLerngruppenLehrer, changes: Partial<UvLerngruppenLehrer>): Promise<void> {
		if (!hatKompetenzAendern.value || loading.value) {
			return;
		}
		const data = { ...lehrerDrafts.value.get(row.id), ...changes };
		lehrerDrafts.value = new Map(lehrerDrafts.value).set(row.id, data);
		if (!lehrerEntwurfGueltig(row, data)) {
			lehrerFehler.value = 'Nicht gespeichert: Reihenfolge oder Stunden sind ungültig. Angerechnete Stunden dürfen die Einsatzstunden nicht überschreiten.';
			return;
		}
		await lehrerPatchesSpeichern([{ id: row.id, data }]);
	}

	function lehrerEntwuerfeSpeichern(): Promise<void> {
		return lehrerPatchesSpeichern(gueltigeLehrerPatches.value);
	}

	async function lehrerPatchesSpeichern(patches: { id: number; data: Partial<UvLerngruppenLehrer> }[]): Promise<void> {
		const id = lerngruppe.id;
		if (!hatKompetenzAendern.value || loading.value || patches.length === 0) {
			return;
		}
		loading.value = true;
		lehrerFehler.value = '';
		try {
			await state.planung.patchLerngruppenLehrerMultiple(patches);
			if (lerngruppe.id === id) {
				const verbleibend = new Map(lehrerDrafts.value);
				for (const patch of patches) {
					verbleibend.delete(patch.id);
				}
				lehrerDrafts.value = verbleibend;
				if (verbleibend.size > gueltigeLehrerPatches.value.length) {
					lehrerFehler.value = 'Einige Lehrkraftänderungen sind noch ungültig oder die Zuordnung fehlt.';
				}
			}
		} catch {
			if (lerngruppe.id === id) {
				lehrerFehler.value = 'Die Lehrkraftänderungen wurden nicht gespeichert. Bitte erneut versuchen.';
			}
		} finally {
			loading.value = false;
		}
	}

	// Bestehende Zuordnungen und speicherbare Änderungen

	const lerngruppenlehrer = computed(() => state.uvManager.lerngruppenLehrerGetMengeByLerngruppe(lerngruppe));

	const gueltigeLehrerPatches = computed(() => {
		const patches: { id: number; data: Partial<UvLerngruppenLehrer> }[] = [];
		for (const row of lerngruppenlehrer.value) {
			const data = lehrerDrafts.value.get(row.id);
			if (data !== undefined && lehrerEntwurfGueltig(row, data)) {
				patches.push({ id: row.id, data });
			}
		}
		return patches;
	});

	async function removeLerngruppenLehrer(item: UvLerngruppenLehrer) {
		const idLerngruppe = lerngruppe.id;
		if (loading.value || !hatKompetenzAendern.value || bearbeitung?.art !== 'lerngruppe-lehrer') {
			return;
		}
		if (planungsManager.value?.lehrerIstFixiert(idLerngruppe, item.idLehrer) === true) {
			return;
		}
		loading.value = true;
		lehrerFehler.value = '';
		try {
			await state.planung.delLerngruppenLehrer([item]);
		} catch {
			lehrerFehler.value = 'Die Änderung wurde nicht gespeichert. Bitte erneut versuchen.';
		} finally {
			loading.value = false;
		}
	}

</script>
