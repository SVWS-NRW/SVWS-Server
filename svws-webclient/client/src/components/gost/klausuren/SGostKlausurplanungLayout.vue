<template>
	<div class="page page-flex-row">
		<div v-if="$slots.sidebar" class="relative h-full min-w-128 max-w-128">
			<div v-if="sidebarDropEnabled" class="pointer-events-none absolute -inset-2 rounded-xl border-2 border-dashed border-ui-danger ring-4 ring-ui-danger/10" />
			<aside class="relative flex h-full flex-col overflow-y-auto rounded-lg"
				@dragover="onSidebarDragover"
				@drop="onSidebarDrop">
				<div v-if="sidebarDropEnabled" class="pointer-events-none absolute inset-0 z-10 flex items-center justify-center px-6 text-center">
					<div class="rounded-lg border border-ui-danger/40 bg-white px-5 py-3 text-headline-md font-bold text-ui-danger shadow-md ring-4 ring-white/80">
						Zum Aufheben der Zuweisung hierher zurücklegen.
					</div>
				</div>
				<div class="flex min-h-0 flex-col gap-2" :class="{'opacity-25': sidebarDropEnabled}">
					<h3 v-if="sidebarTitle !== undefined" class="text-headline-md leading-none" :title="sidebarTitle">
						{{ sidebarTitle }}
					</h3>
					<slot name="sidebar" />
				</div>
			</aside>
		</div>
		<div class="grow flex flex-col gap-4 h-full w-full overflow-hidden">
			<slot name="workspace" />
		</div>
		<div v-if="$slots.aside" class="flex flex-col h-full overflow-y-auto min-w-96 max-w-96">
			<slot name="aside" />
		</div>
	</div>
</template>

<script setup lang="ts">
	const props = withDefaults(defineProps<{
		sidebarTitle?: string;
		sidebarDropEnabled?: boolean;
	}>(), {
		sidebarTitle: undefined,
		sidebarDropEnabled: false,
	});

	const emit = defineEmits<{
		sidebarDrop: [event: DragEvent];
	}>();

	function onSidebarDragover(event: DragEvent): void {
		if (!props.sidebarDropEnabled) {
			return;
		}
		event.preventDefault();
	}

	function onSidebarDrop(event: DragEvent): void {
		if (!props.sidebarDropEnabled) {
			return;
		}
		emit("sidebarDrop", event);
	}
</script>
