<template>
	<svws-ui-content-card title="Grundschulbesuch">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-input-number placeholder="Einschulung" :model-value="data.grundschuleEinschulungsjahr" @change="grundschuleEinschulungsjahr => patch({ grundschuleEinschulungsjahr })" />
			<svws-ui-input-number placeholder="Einschulungsart" :model-value="data.grundschuleEinschulungsartID" @change="grundschuleEinschulungsartID => patch({ grundschuleEinschulungsartID })" />
			<svws-ui-input-number placeholder="EP-Jahre" :model-value="data.grundschuleJahreEingangsphase" @change="grundschuleJahreEingangsphase => patch({ grundschuleJahreEingangsphase })" />
			<svws-ui-input-number placeholder="Übergangsempfehlung Jg. 5" :model-value="data.grundschuleUebergangsempfehlungID" @change="grundschuleUebergangsempfehlungID => patch({ grundschuleUebergangsempfehlungID })" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerSchulbesuchsdaten } from "@core";

	defineProps<{
		data: SchuelerSchulbesuchsdaten;
		patch: (data : Partial<SchuelerSchulbesuchsdaten>) => Promise<void>;
	}>();

</script>
