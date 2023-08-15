<template>
	<svws-ui-input-wrapper :grid="4">
		<div class="text-headline-md text-left">
			<span>{{ Wochentag.fromIDorException(item.id) }}</span>
		</div>
	</svws-ui-input-wrapper>
	<svws-ui-button type="danger" @click="removeZeitraster([...stundenplanManager().getListZeitrasterZuWochentag(item)])"> {{ item.beschreibung }} mit allen Stunden entfernen </svws-ui-button>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster} from "@core";
	import { Wochentag } from "@core";

	const props = defineProps<{
		item: Wochentag;
		stundenplanManager: () => StundenplanManager;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

</script>
