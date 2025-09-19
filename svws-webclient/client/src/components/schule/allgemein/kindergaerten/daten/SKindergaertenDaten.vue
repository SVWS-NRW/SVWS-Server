<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" span="full" placeholder="Bezeichnung" :model-value="manager().daten().bezeichnung"
					:readonly :max-len="100" :min-len="1" @change="v => patch({ bezeichnung: v?.trim() ?? undefined })" />
				<svws-ui-text-input placeholder="Bemerkung" span="full" :model-value="manager().daten().bemerkung"
					:readonly :max-len="50" @change="v => patch({ bemerkung: v?.trim() ?? undefined })" />
				<svws-ui-text-input placeholder="Telefon" :readonly :model-value="manager().daten().tel" @change="v => patch( { tel: v?.trim() })"
					type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail-Adresse" :readonly :model-value="manager().daten().email" :max-len="40"
					@change="patchEmail" type="email" verify-email />
				<svws-ui-input-number placeholder="Sortierung" :model-value="manager().daten().sortierung" :readonly
					@change="value => patch({ sortierung: value === null ? 32000 : value })" />
				<svws-ui-checkbox :readonly :model-value="manager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })">
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<div />
		<svws-ui-content-card title="Adresse">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße" :readonly :model-value="adresse" @change="patchStrasse" span="full" :max-len="55" />
				<svws-ui-text-input placeholder="PLZ" :model-value="manager().daten().plz" @change="v => patch({ plz: v?.trim() })" :max-len="10" />
				<svws-ui-text-input placeholder="Ort" :model-value="manager().daten().ort" @change="v => patch({ ort: v?.trim() })" :max-len="30" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { KindergaertenDatenProps } from "~/components/schule/allgemein/kindergaerten/daten/SKindergaertenDatenProps";
	import { AdressenUtils, BenutzerKompetenz } from "@core";
	import { computed } from "vue";
	import { emailIsValid } from "~/util/validation/Validation";

	const props = defineProps<KindergaertenDatenProps>()
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed<boolean>(() => !hatKompetenzUpdate.value);

	const adresse = computed(() => AdressenUtils.combineStrasse(
		props.manager().daten().strassenname?.trim() ?? "", props.manager().daten().hausNr?.trim() ?? "", props.manager().daten().hausNrZusatz?.trim() ?? ""))

	const patchStrasse = (value: string | null) => {
		const vals = AdressenUtils.splitStrasse(value);
		void props.patch({ strassenname: vals[0], hausNr: vals[1], hausNrZusatz: vals[2] });
	}

	const patchEmail = (value: string | null) => {
		if (emailIsValid(value, 40))
			void props.patch({ email: value?.trim() });
	}

</script>
