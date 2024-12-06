<template>
	<svws-ui-content-card>
		<svws-ui-input-wrapper>
			<svws-ui-text-input class="contentFocusField" :model-value="getBenutzergruppenManager().getBezeichnung()" @change="setBezeichnung" type="text" placeholder="Bezeichnung" />
			<svws-ui-checkbox type="toggle" v-model="inputIstAdmin"> Alle Kompetenzen freigeben </svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type {BenutzergruppenManager} from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		getBenutzergruppenManager: () => BenutzergruppenManager;
		setBezeichnung : (anzeigename: string | null) => Promise<void>;
		setIstAdmin : (istAdmin:boolean) => Promise<void>;
	}>();

	const inputIstAdmin = computed<boolean>({
		get: () => props.getBenutzergruppenManager().istAdmin(),
		set: (value) => {
			if (value === props.getBenutzergruppenManager().istAdmin())
				return;
			void props.setIstAdmin(value);
		}
	});

</script>
