<template>
	<svws-ui-content-card title="Grunddaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Bezeichnung" :model-value="stundenplanManager().getBezeichnungStundenplan()"
				@update:model-value="patch({ bezeichnungStundenplan: String($event) })" type="text" span="full" />
			<svws-ui-text-input placeholder="Gültig ab" :model-value="stundenplanManager().getGueltigAb()"
				@update:model-value="patch({ gueltigAb: String($event) })" type="date" />
			<svws-ui-text-input placeholder="Gültig bis" :model-value="stundenplanManager().getGueltigBis()"
				@update:model-value="patch({ gueltigBis: String($event) })" type="date" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Stundenplan, StundenplanManager } from "@core";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patch: (data: Partial<Stundenplan>) => Promise<void>;
	}>();

</script>
