<template>
	<svws-ui-input-wrapper :grid="2" class="text-left">
		<svws-ui-text-input placeholder="Anrede" v-model="model.proxy.anrede" type="text" :max-len="20" :readonly />
		<svws-ui-text-input placeholder="Titel" v-model="model.proxy.titel" type="text" :max-len="10" :readonly />
		<svws-ui-text-input placeholder="Rufname" v-model="model.proxy.vorname"
			:validation="() => model.getFehler('vorname')" type="text" :max-len="80" required :readonly />
		<svws-ui-text-input placeholder="Nachname" v-model="model.proxy.nachname"
			:validation="() => model.getFehler('nachname')" type="text" :max-len="120" required :readonly />
		<svws-ui-text-input placeholder="E-Mail Adresse" v-model="model.proxy.eMail"
			:validation="() => model.getFehler('eMail')" type="email" verify-email :max-len="100" :readonly />
		<ui-select label="Staatsangehörigkeit"
			v-model="model.staatsangehoerigkeit.value"
			:manager="staatsangehoerigkeitenManager"
			:readonly searchable />
	</svws-ui-input-wrapper>
</template>

<script setup lang="ts">
	import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { computed } from "vue";
	import type { ErzieherStammdatenModelProxy } from "~/components/schueler/erziehungsberechtigte/modelproxy/ErzieherStammdatenModelProxy";

	const props = defineProps<{
		model: ErzieherStammdatenModelProxy;
		schuljahr: number;
		readonly?: boolean;
	}>();

	const readonly = computed(() => props.readonly);

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: computed(() => props.schuljahr),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});
</script>
