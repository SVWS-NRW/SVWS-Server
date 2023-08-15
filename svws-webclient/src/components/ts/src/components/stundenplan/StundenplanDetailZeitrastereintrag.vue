<template>
	<svws-ui-input-wrapper :grid="4">
		<div class="text-headline-md text-left">
			<span>{{ Wochentag.fromIDorException(item.wochentag) }}</span>
		</div>
		<svws-ui-text-input :model-value="item.stundenbeginn" required placeholder="Stundenbeginn" @update:model-value="patchZeitraster({stundenbeginn: Number($event)}, item)" />
		<svws-ui-text-input :model-value="item.stundenende" placeholder="Stundenende" @update:model-value="patchZeitraster({stundenende: Number($event)}, item)" />
	</svws-ui-input-wrapper>
	<svws-ui-button type="danger" @click="removeZeitraster([item])"> Eintrag entfernen </svws-ui-button>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster} from "@core";
	import { Wochentag } from "@core";

	const props = defineProps<{
		item: StundenplanZeitraster;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

</script>
