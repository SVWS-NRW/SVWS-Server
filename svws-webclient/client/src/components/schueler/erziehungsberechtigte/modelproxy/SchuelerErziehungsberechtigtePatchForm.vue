<template>
	<svws-ui-content-card :title="(model.proxy.vorname !== null) || (model.proxy.nachname !== null)
		? `Daten zu ${model.proxy.vorname ? model.proxy.vorname + ' ' : ''}${model.proxy.nachname}`
		: 'Daten zur Person'">
		<template #actions>
			<svws-ui-checkbox class="mr-2"
				:model-value="model.proxy.erhaeltAnschreiben ?? false"
				@update:model-value="val => model.proxy.erhaeltAnschreiben = val"
				:readonly>
				Erhält Anschreiben
			</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<ui-select label="Erzieherart"
				v-model="model.erzieherart.value"
				:manager="erzieherartenManager"
				:validation="() => model.getFehler('idErzieherArt')"
				:removable="false" :readonly searchable />
			<svws-ui-text-input placeholder="Anrede"
				v-model="model.proxy.anrede"
				:validation="() => model.getFehler('anrede')"
				@change="model.patch"
				:max-len="20" :readonly />
			<svws-ui-text-input placeholder="Titel"
				v-model="model.proxy.titel"
				:validation="() => model.getFehler('titel')"
				@change="model.patch"
				:max-len="10" :readonly />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Name"
				v-model="model.proxy.nachname"
				:validation="() => model.getFehler('nachname')"
				@change="model.patch"
				:max-len="120" :readonly required />
			<svws-ui-text-input placeholder="Rufname"
				v-model="model.proxy.vorname"
				:validation="() => model.getFehler('vorname')"
				@change="model.patch"
				:max-len="80" :readonly required />
			<svws-ui-text-input placeholder="E-Mail Adresse" type="email"
				v-model="model.proxy.eMail"
				:validation="() => model.getFehler('eMail')"
				@change="model.patch"
				:max-len="100" :readonly />
			<svws-ui-spacing />
			<ui-select label="Staatsangehörigkeit"
				v-model="model.staatsangehoerigkeit.value"
				:manager="staatsangehoerigkeitenManager"
				:readonly searchable />
			<svws-ui-text-input placeholder="Straße und Hausnummer"
				v-model="model.adresse.value"
				:validation="() => model.getFehler('strassenname')"
				@change="model.patch"
				:readonly />
			<ui-select label="Wohnort"
				v-model="model.wohnort.value"
				:manager="wohnortManager"
				:readonly searchable />
			<ui-select label="Ortsteil"
				v-model="model.ortsteil.value"
				:manager="ortsteilManager"
				:readonly="readonly || !model.proxy.wohnortID"
				searchable />
			<svws-ui-spacing />
			<svws-ui-textarea-input placeholder="Bemerkungen"
				v-model="model.proxy.bemerkungen"
				:validation="() => model.getFehler('bemerkungen')"
				@change="model.patch"
				:readonly
				span="full" autoresize />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { ErzieherStammdatenModelProxy } from "~/components/schueler/erziehungsberechtigte/modelproxy/ErzieherStammdatenModelProxy";
	import type { Erzieherart, ErzieherStammdaten } from "@core";
	import { Nationalitaeten } from "@core";
	import { CoreTypeSelectManager, SelectManager, useOrteState } from "@ui";
	import { erzieherArtSort, orte_sort, ortsteilSort } from "~/utils/helfer";
	import { computed } from "vue";

	const props = defineProps<{
		erzieher: ErzieherStammdaten;
		erzieherartenById: Map<number, Erzieherart>;
		schuljahr: number;
		hatKompetenzUpdate: boolean;
		patch: (data: Partial<ErzieherStammdaten>, id: number) => Promise<void>;
	}>();

	const orteState = useOrteState();

	const readonly = computed(() => !props.hatKompetenzUpdate);
	const model = new ErzieherStammdatenModelProxy(
		() => props.erzieher,
		() => props.erzieherartenById,
		() => props.schuljahr,
		async (data) => {
			await props.patch(data, props.erzieher.id); return true;
		}
	);

	const erzieherartenManager = new SelectManager({
		options: computed(() => props.erzieherartenById.values()),
		sort: erzieherArtSort,
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: computed(() => props.schuljahr),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const wohnortManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		sort: orte_sort,
		optionDisplayText: i => `${i.plz} ${i.ortsname}`,
		selectionDisplayText: i => `${i.plz} ${i.ortsname}`,
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.listByOrtId(model.proxy.wohnortID)),
		sort: ortsteilSort,
		optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '',
	});

</script>
